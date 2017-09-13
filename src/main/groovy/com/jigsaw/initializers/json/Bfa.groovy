package com.jigsaw.initializers.json

import com.jigsaw.initializers.Initializer
import com.jigsaw.utilities.request.SimpleServletConfig
import com.jigsaw.utilities.request.SimpleServletRequest
import com.sonyericsson.jenkins.plugins.bfa.PluginImpl
import hudson.util.IOUtils
import net.sf.json.JSONObject
import org.kohsuke.stapler.*

import javax.servlet.ServletConfig
import javax.servlet.http.HttpServletRequest

class Bfa extends Initializer {

    PluginImpl plugin

    Bfa(PluginImpl plugin) {
        this.plugin = plugin
    }

    void init(Map env) {
        InputStream is = new FileInputStream("${env['JENKINS_INIT']}/etc/json/Bfa.json")
        String jsonTxt = IOUtils.toString(is)
        System.out.println(jsonTxt)
        JSONObject jsonBfaConfig = JSONObject.fromObject(jsonTxt)

        HttpServletRequest httpServletRequest = new SimpleServletRequest()
        ServletConfig servletConfig = new SimpleServletConfig()
        Stapler stapler = new Stapler()
        stapler.init(servletConfig)
        WebApp wa = stapler.getWebApp()
        wa.bindInterceptors.add(0, new BindInterceptor())
        StaplerRequest req = new RequestImpl(stapler, httpServletRequest, new ArrayList<AncestorImpl>(), null)
        this.plugin.configure(req, jsonBfaConfig)
    }
}
