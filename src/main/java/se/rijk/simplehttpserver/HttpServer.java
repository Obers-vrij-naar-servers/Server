package se.rijk.simplehttpserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.rijk.simplehttpserver.config.Configuration;
import se.rijk.simplehttpserver.config.ConfigurationManager;
import se.rijk.simplehttpserver.core.ServerListenerThread;

/**
 *
 * Driver Class for the Http Server
 *
 */
public class HttpServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    public static void main(String[] args){
        LOGGER.info("Server Starting...");

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/config.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        LOGGER.info("Using Port: " + conf.getPort());
        LOGGER.info("Using Webroot: " + conf.getWebroot());

        try{
            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(),conf.getWebroot());
            serverListenerThread.start();
        }catch (Exception e){
            e.printStackTrace();
            //TODO handle error
        }
    }
}
