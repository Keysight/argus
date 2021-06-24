import inspect


class SonicTest:
    def __init__(self, *args, **kwargs):
        self.funcargs = kwargs['funcargs']
        self.log = kwargs['log']

    def get_product(self):
        return "SONiC", self.funcargs['duthost'].os_version

    def get_topology(self):
        return self.funcargs['tbinfo']['topo']['name']

    def get_hwsku(self):
        return self.funcargs['duthost'].facts['hwsku']

    def get_sn(self):
        return self.funcargs['duthost']._run_on_asics("sudo", "decode-syseeprom", "-s")['stdout']

    def get_dutip(self):
        return self.funcargs['duthost'].mgmt_ip

    def get_interfaces(self):
        return ";".join(list(self.funcargs['conn_graph_facts']['device_conn'][u'sonicdut'].keys()))

    def get_speed(self):
        return ";".join(list(set([v['speed'] for _, v in self.funcargs['conn_graph_facts']['device_conn'][u'sonicdut'].items()])))

    def get_tbip(self):
        hvars = self.funcargs['localhost'].host.options['variable_manager'].get_vars()['hostvars']
        tb_ip = "NA"
        for k, v in hvars.items():
            if "external_port" in v:
                tb_ip = v["ansible_host"]
                break
        return tb_ip
