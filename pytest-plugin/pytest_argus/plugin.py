#!/usr/bin/env python
# -*- coding: utf-8 -*-
from __future__ import absolute_import, division, print_function,unicode_literals
import datetime
import errno
import hashlib
import io
import os
import platform
import sys
import time

import pytest
import yaml

sys.path.append(os.path.dirname(__file__))
import importlib
import logging

import mysql.connector
from _pytest.runner import pytest_runtest_setup, pytest_runtest_teardown,runtestprotocol


_current_error = ''
_suite_name = None
_test_name = None
_test_status = None
_test_start_time = None
_duration = 0
_initial_trigger = True
pytest_argus = True
run_key = None


class NewLineFormatter(logging.Formatter):
    def __init__(self, fmt, datefmt=None):
        """Init given the log line format and date format"""
        logging.Formatter.__init__(self, fmt, datefmt)

    def format(self, record):
        """Override format function"""
        msg = logging.Formatter.format(self, record)
        if record.message != "":
            parts = msg.split(record.message)
            msg = msg.replace('\n', '\n' + parts[0])
        return msg


def get_logger():
    global log
    LOG_FILENAME = '.'.join([os.path.basename(sys.argv[0]).split('.')[0], 'log'])
    logging.root.handlers = []
    fmt = NewLineFormatter("%(asctime)s - %(levelname)-10s — %(funcName)s:%(lineno)d - %(message)s", datefmt="%Y-%m-%d %H:%M:%S")
    # File handler
    fileHandler = logging.FileHandler(LOG_FILENAME)
    fileHandler.setFormatter(fmt)
    # console handler
    sHandler = logging.StreamHandler()
    sHandler.setFormatter(fmt)

    logging.basicConfig(level=logging.DEBUG if debug else logging.INFO, handlers=[fileHandler, sHandler])
    log = logging.getLogger(LOG_FILENAME)
    logging.getLogger('requests.packages.urllib3.connectionpool').propagate = 0
    logging.getLogger('requests.packages.urllib3.connectionpool').addHandler(logging.NullHandler())
    log.debug('ARGUS:plugin:%s' % sys._getframe().f_code.co_name)


class dict2Obj(object):
    """Description: Convert dictionaries to Object"""

    def __init__(self, vdict):
        self.__mydict__ = vdict if isinstance(vdict, dict) else {}

    def __getattr__(self, key, verbose=True):
        value = self.__mydict__.get(key, {})
        if isinstance(value, dict):
            return dict2Obj(value)
        else:
            return value

    def __str__(self):
        s = ['.{key} = {value}\n'.format(key=key, value=repr(value)) for key, value in self.__mydict__.items()]
        return ''.join(s)

    def hasattr(self, key):
        if key in self.__mydict__.keys():
            return True
        return False


def getTestClass(*args, **kwargs):
    if test_type:
        modname = test_type.lower() + "." + test_type.lower()
    else:
        raise Exception('Fail to load module %s' % modname)
    try:
        imod = importlib.import_module(modname)
        cls = getattr(imod, test_type.title() + "Test")
        return cls(*args, **kwargs)
    except:
        raise Exception('Fail to load module %s' % modname)


log, ttobj, debug = None, None, True

db = dict2Obj(yaml.load(open(os.path.join(os.path.dirname(__file__), 'settings.yaml')), Loader=yaml.FullLoader))
db_host = db.database.host
db_user = db.database.user
db_pass = db.database.password
db_name = db.database.dbname
test_type = db.database.test_type

get_logger()


@pytest.hookimpl()
def pytest_sessionstart(session):
    global pytest_argus
    #pytest_argus = session.config.option.argus
    log.debug("PyTest Session Start")
    log.debug("PyTest Session Start: %s" % __file__)
    if pytest_argus == 'False':
        return
    global con
    con = db_connect(db_host, db_user, db_pass, db_name)
    # insert values into execution table


def pytest_runtest_setup(item):
    log.debug("PyTest RunTest Setup")
    if pytest_argus == 'False':
        return


@pytest.hookimpl()
def pytest_runtest_call(item):
    log.debug("PyTest RunTest Call")
    global ttobj, run_key, _test_start_time
    _test_start_time = time.time()
    log.debug("*" * 100)
    for key in dir(item):
        log.debug(key + "=" + str(getattr(item, key)))
    log.debug("*" * 100)
    run_name = 'test2'
    ttobj = getTestClass(funcargs=item.funcargs, log=log)
    if not run_key:
        log.debug("PyTest RunTest INsert Run Key")
        run_key = db_insert_new_run(con, run_name)
        p, v = ttobj.get_product()
        db_insert_product_version(con, run_key, p, v)


@pytest.hookimpl(tryfirst=True, hookwrapper=True)
def pytest_runtest_makereport(item, call):
    outcome = yield
    log.debug("PyTest RunTest MakeReport")
    if pytest_argus == 'False':
        return
    rep = outcome.get_result()

    global _suite_name
    _suite_name = rep.nodeid.split('::')[0]

    if _initial_trigger:
        set_initial_trigger()

    if rep.when == 'call' and rep.passed:
        if hasattr(rep, 'wasxfail'):
            update_test_status('xPASS')
            global _current_error
            update_test_error('')
        else:
            update_test_status('PASS')
            update_test_error('')

    if rep.failed:
        if getattr(rep, 'when', None) == 'call':
            if hasattr(rep, 'wasxfail'):
                update_test_status('xPASS')
                update_test_error('')
            else:
                update_test_status('FAIL')
                if rep.longrepr:
                    for line in rep.longreprtext.splitlines():
                        exception = line.startswith('E   ')
                        if exception:
                            update_test_error(line.replace('E    ', ''))
        else:
            update_test_status('ERROR')
            if rep.longrepr:
                for line in rep.longreprtext.splitlines():
                    update_test_error(line)

    if rep.skipped:
        if hasattr(rep, 'wasxfail'):
            update_test_status('xFAIL')
            if rep.longrepr:
                for line in rep.longreprtext.splitlines():
                    exception = line.startswith('E   ')
                    if exception:
                        update_test_error(line.replace('E    ', ''))
        else:
            update_test_status('SKIP')
            if rep.longrepr:
                for line in rep.longreprtext.splitlines():
                    update_test_error(line)


def pytest_runtest_teardown(item, nextitem):
    global _test_name, _duration
    log.debug("PyTest RunTest TearDown")
    if pytest_argus == 'False':
        return
    _test_end_time = time.time()
    _test_name = item.name
    _duration = 0
    try:
        _duration = _test_end_time - _test_start_time
    except Exception as e:
        print(e)
    # create list to save content
    insert_test_results(item)


def pytest_sessionfinish(session):
    log.debug("PyTest RunTest SessionFinish")
    if pytest_argus == 'False':
        return
    # insert_suite_results(_suite_name)
    reset_counts()


@pytest.hookimpl(hookwrapper=True)
def pytest_terminal_summary(terminalreporter, exitstatus, config):
    yield
    log.debug("PyTest Terminal Summary")
    if pytest_argus == 'False':
        return
    db_update_run(con, run_key)


def insert_suite_results(name):
    log.debug("PyTest RunTest insert_suite_results")
    _sexecuted = _spass_tests + _sfail_tests + _sxpass_tests + _sxfail_tests
    insert_into_suite_table(con, run_key, str(name), _sexecuted, _spass_tests, _sfail_tests, _sskip_tests, _sxpass_tests, _sxfail_tests, _serror_tests)


def insert_test_results(item):
    log.debug("PyTest RunTest insert_test_results")
    nodeid = item.rep_setup.nodeid
    setup_type = str(ttobj.get_hwsku())
    setup_sn = str(ttobj.get_sn())
    setup_address = str(ttobj.get_dutip())
    test_topology = str(ttobj.get_topology())
    intrf = str(ttobj.get_interfaces())
    intrf_sp = str(ttobj.get_speed())
    tb_ip = str(ttobj.get_tbip())
    bs, ex = os.path.split(item.location[0])
    suite = bs if bs else "[ROOT]"
    md5 = hash_md5(str(item.fspath))
    log.debug("MD5:" + str(md5))
    db_insert_into_test_table(con, run_key, nodeid, str(_test_status), round(_duration, 2), str(_current_error), test_topology, setup_type, setup_sn, setup_address, md5, suite, intrf, intrf_sp, tb_ip)
    log.debug("Insert Done")


def set_initial_trigger():
    global _initial_trigger
    _initial_trigger = False


def update_test_error(msg):
    global _current_error
    _current_error = msg


def update_test_status(status):
    global _test_status
    _test_status = status


def hash_md5(filename):
    md5 = hashlib.md5()
    bsize = os.statvfs(os.getcwd()).f_bsize
    with io.open(filename, 'rb') as f:
        for chunk in iter(lambda: f.read(bsize), b''):
            md5.update(chunk)
    return md5.hexdigest().upper()


def db_connect(db_host, db_user, db_pass, db_name):
    try:
        mydb = mysql.connector.connect(
            host=db_host,
            user=db_user,
            passwd=db_pass,
            database=db_name
        )
        return mydb
    except Exception:
        print('''Couldn't connect to Database''')
        print(Exception)


def db_insert_new_run(con, run_name):
    log.debug("PyTest RunTest db_insert_new_run")

    cursor = con.cursor()
    sql = '''INSERT INTO `ARGUS`.`b_runs` ''' + \
        '''(`run_owner`, `run_name`, `total_count_end`) VALUES ''' + \
        '''(%s,           %s,         %s);'''
    run_owner = 'test'
    total_count_end = 0
    val = (run_owner, run_name, total_count_end)
    cursor.execute(sql, val)
    KEY = cursor.lastrowid
    con.commit()
    return KEY


def db_update_run(con, run_key):
    cursor = con.cursor()
    sql = '''UPDATE `ARGUS`.`b_runs` SET \
        `run_end_date` = now() \
        WHERE `KEY` = %s \
    '''
    val = (run_key, )
    cursor.execute(sql, val)
    con.commit()


def db_insert_product_version(con, run_key, product_name, product_value):
    log.debug("PyTest RunTest db_insert_product_version")
    cursor = con.cursor()
    sql = '''INSERT INTO `ARGUS`.`b_builds` ''' + \
        '''(`run_key`, `product_name`, `product_value`) VALUES ''' + \
        '''(%s,         %s,             %s);'''
    val = (run_key, product_name, product_value)
    cursor.execute(sql, val)
    con.commit()

#eid, test, status, duration, msg


def db_insert_into_test_table(con, run_key, test, status, duration, msg, test_topology, setup_type, setup_sn, setup_address, md5, suite, intrf, intrf_sp, tb_ip):
    log.debug("PyTest RunTest db_insert_into_test_table")

    cursor = con.cursor()
    #sql = 'INSERT INTO TB_TEST (Test_Id, Execution_Id, Test_Name, Test_Status, Test_Time, Test_Error) VALUES (%s, %s, %s, %s, %s, %s)'
    sql = '''INSERT INTO `ARGUS`.`b_tests` ''' + \
        '''(`run_key`, `test_location`, `test_name`, `test_result`, `test_log`, `test_error`, `test_duration`, `test_client_address`, `test_client_port`, `test_topology`, `test_hash_code`, `suite`, `setup_type`, `setup_sn`, `setup_address`, `setup_ports`, `ports_type`) VALUES ''' + \
        '''(%s,         %s,             %s,          %s,           '2',         %s,           %s,             %s,                   '4',                %s,             %s,                    %s,              %s,         %s,       %s,            %s,          %s);'''
    val = (run_key, test, _test_name, status, msg, duration, tb_ip, test_topology, md5, suite, setup_type, setup_sn, setup_address, intrf, intrf_sp)
    cursor.execute(sql, val)
    con.commit()


def getMD5(filePath):
    md5 = hashlib.md5()
    bsize = os.statvfs(os.getcwd()).f_bsize  # for performance reasons we will read same block size as the os default
    with io.open(filePath, 'rb') as fh:
        for chunk in iter(lambda: fh.read(bsize), b''):
            md5.update(chunk)
    str = md5.hexdigest()
    return str.upper()
