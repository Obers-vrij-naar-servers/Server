package se.rijk.afspserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.rijk.afspserver.config.Configuration;
import se.rijk.afspserver.config.ConfigurationManager;
import se.rijk.afspserver.core.ServerListenerThread;

/**
 *
 * Driver Class for the AFSP-Server
 *
 */
public class AfspServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(AfspServer.class);
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
