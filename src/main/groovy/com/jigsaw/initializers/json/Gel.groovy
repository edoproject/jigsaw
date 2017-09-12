package com.jigsaw.initializers.json

import com.jigsaw.initializers.Initializer
import hudson.util.IOUtils
import net.sf.json.JSONObject
import org.jenkinsci.plugins.globalEventsPlugin.GlobalEventsPlugin

class Gel extends Initializer {
    GlobalEventsPlugin plugin

    Gel(GlobalEventsPlugin plugin) {
        this.plugin = plugin
    }

    void init(Map env) {
        InputStream is = new FileInputStream("${env['JENKINS_INIT']}/etc/json/gel.json")
        String jsonTxt = IOUtils.toString(is)
        System.out.println(jsonTxt)
        JSONObject jsonConfig = JSONObject.fromObject(jsonTxt)

        String initScript = new File("${env['JENKINS_INIT']}/scripts/${jsonConfig.get("onEventGroovyCode")}").text
        jsonConfig.put("onEventGroovyCode", initScript)

        plugin.getDescriptor().configure(null, jsonConfig)
    }
}
