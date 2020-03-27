package de.rnoennig.config;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
@WebFilter(value = "/*", initParams = {
        @WebInitParam(name = "applicationClassName",
                value = "de.rnoennig.config.WicketApplication"),
        @WebInitParam(name = "configuration", value = "development") })
public class MyWicketFilter extends org.apache.wicket.protocol.http.WicketFilter {
}
