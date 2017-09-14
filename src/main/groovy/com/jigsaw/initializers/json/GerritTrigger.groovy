package com.jigsaw.initializers.json

import com.jigsaw.initializers.Initializer
import com.sonyericsson.hudson.plugins.gerrit.trigger.GerritServer
import com.sonyericsson.hudson.plugins.gerrit.trigger.PluginImpl
import com.sonyericsson.hudson.plugins.gerrit.trigger.config.IGerritHudsonTriggerConfig
import hudson.util.IOUtils
import net.sf.json.JSONObject

import java.util.concurrent.CopyOnWriteArrayList

class GerritTrigger extends Initializer {

    PluginImpl plugin

    GerritTrigger(PluginImpl plugin) {
        this.plugin = plugin
    }

    @Override
    void init(Map env) {
        super.init(env)
        InputStream is = new FileInputStream("${env['JENKINS_INIT']}/etc/json/gerritTrigger.json")
        String jsonTxt = IOUtils.toString(is)
        System.out.println(jsonTxt)
        JSONObject jsonGerritTriggerConfig = JSONObject.fromObject(jsonTxt)

        IGerritHudsonTriggerConfig gerritServersConfig = new com.sonyericsson.hudson.plugins.gerrit.trigger.config.Config()
        gerritServersConfig.setValues(jsonGerritTriggerConfig)

        GerritServer gerritServer = new GerritServer(jsonGerritTriggerConfig.getString('gerritHostName'), false)
        gerritServer.setConfig(gerritServersConfig)
        List<GerritServer> gerritServers = new CopyOnWriteArrayList<GerritServer>()
        gerritServers.add(gerritServer)
        plugin.setServers(gerritServers)
    }

    @Override
    void start(Map env) {
        sleep 5000
        List<GerritServer> gerritServers = plugin.getServers()
        GerritServer gerritServer = gerritServers[0]
        gerritServer.stop()
        gerritServer.start()
        gerritServer.startConnection()
    }
}
