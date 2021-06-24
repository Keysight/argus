.. image:: https://img.shields.io/pypi/v/pytest_argus.svg
    :target: https://pypi.org/project/pytest_argus

.. image:: https://img.shields.io/pypi/pyversions/pytest_argus.svg
    :target: https://pypi.org/project/pytest_argus

.. image:: https://img.shields.io/badge/license-MIT-green.svg
    :target: https://en.wikipedia.org/wiki/MIT_License




pytest_argus is the Python plugin for pytest that colects results and stores them.

Installing
==========

| The main branch always contains the latest official release. Official releases are posted to `PyPI <https://pypi.python.org/pypi/pytest_argus/>`_. 
| The dev branch contains improvements and fixes of the current release that will go into the next release version.

* To install the official release just run
  ``pip install --upgrade pytest_argus``.
* To install the version in `github <https://github.com/keysight/argus/pytest-plugin>`_ use
  ``python setup.py develop`` for development install or
  ``python setup.py install``.

Testing
=======
| Unit tests can be invoked by running ``python setup.py test`` command.
| We strongly recommend that you test the package installation and unit test execution against the python environments listed in ''tox.ini''.
| For this you can use `tox <https://testrun.org/tox/>`_ utility. Run the following:

* yum install python-tox
* tox

Documentation
=============
| For general documentation see <https://github.com/keysight/argus>


pytest_argus / Python Support
=====================================
pytest_argus supports:

* Python 3.3, 3.4, 3.5, 3.6, 3.7, 3.8, 3.9

Compatibility with older versions may continue to work but it is not actively supported.
