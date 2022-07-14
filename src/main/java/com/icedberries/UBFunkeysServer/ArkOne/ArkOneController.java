package com.icedberries.UBFunkeysServer.ArkOne;

import com.icedberries.UBFunkeysServer.ArkOne.Plugins.BasePlugin;
import com.icedberries.UBFunkeysServer.ArkOne.Plugins.GalaxyPlugin;
import com.icedberries.UBFunkeysServer.ArkOne.Plugins.UserPlugin;
import javagrinko.spring.tcp.Connection;
import javagrinko.spring.tcp.TcpController;
import javagrinko.spring.tcp.TcpHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@TcpController
public class ArkOneController implements TcpHandler {

    public static final String IP_ADDRESS = "127.0.0.1";

    // Plugins
    @Autowired
    BasePlugin basePlugin;

    @Autowired
    UserPlugin userPlugin;

    @Autowired
    GalaxyPlugin galaxyPlugin;

    @Override
    public void receiveData(Connection connection, byte[] data) {
        // Log the received request
        String xmlData = new String(data);
        System.out.println("[ArkOne] New Request: " + xmlData);

        // Create a list of responses to send back
        ArrayList<String> responses = new ArrayList<>();

        // Parse the incoming data into individual commands
        List<String> commands = ArkOneParser.ParseReceivedMessage(xmlData);

        // Handle each command
        for (String command : commands) {
            try {
                Element commandInfo = (Element)ArkOneParser.ParseCommand(command);
                switch(commandInfo.getNodeName()) {
                    // Plugin 0 - Core
                    case "a_lgu":
                        responses.add(basePlugin.LoginGuestUser());
                        break;
                    case "a_gpd":
                        responses.add(basePlugin.GetPluginDetails(commandInfo.getAttribute("p")));
                        break;
                    case "a_gsd":
                        responses.add(basePlugin.GetServiceDetails(commandInfo.getAttribute("s")));
                        break;
                    case "a_lru":
                        responses.add(basePlugin.LoginRegisteredUser(commandInfo));
                        break;

                    // Plugin 1 (User)
                    case "u_reg":
                        responses.add(userPlugin.RegisterUser(commandInfo));
                        break;

                    // Plugin 7 (Galaxy)
                    case "lpv":
                        responses.add(galaxyPlugin.LoadProfileVersion());
                        break;

                    // Plugin 10 (Trunk)

                    // Catch Unhandled Commands
                    default:
                        responses.add("<unknown />");
                        System.out.println("[ArkOne][ERROR] Unhandled command: " + commandInfo.getNodeName());
                        break;
                }
            } catch (Exception e) {
                System.out.println("[ArkOne][ERROR] Unknown error occurred: ");
                e.printStackTrace();
                responses.add("<unknown />");
            }
        }

        // Send the response
        for(String response : responses) {
            try {
                // Append a 0x00 to the end of the response
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                outputStream.write(response.getBytes());
                outputStream.write((byte)0x00);
                byte[] combinedResponse = outputStream.toByteArray();

                connection.send(combinedResponse);

                System.out.println("[ArkOne] Response: " + response);
            } catch (IOException e) {
                System.out.println("[ArkOne][ERROR] Unknown error occurred: ");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void connectEvent(Connection connection) {
        // No need to log anything
    }

    @Override
    public void disconnectEvent(Connection connection) {
        // No need to log anything
    }
}