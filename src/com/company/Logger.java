package com.company;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Logger implements IMqttMessageListener {

    String topic;
    String content;
    int qos;
    String broker;
    String clientId;
    MqttClient client;
    BufferedWriter writer;

    public Logger() {                                           //Skapar ny MQTT klient här
        topic = "myLivingroom/mySquareMeter/#";
        content = "Message from MqttPublishSample";
        qos = 2;
        broker = "tcp://broker.hivemq.com";
        clientId = "Alexandra och Soleimans fantastiska temperaturloggingsmakapär";
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            client.connect(options);
            client.subscribe(topic, this);
            writer = new BufferedWriter(new FileWriter("Log.txt"));

        } catch (MqttException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main (String args[]){
        Logger logger = new Logger();
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws IOException {
        System.out.print(s + " ");
        System.out.println(mqttMessage.toString());
        logMessage(s, mqttMessage.toString());
    }

    private void logMessage(String s, String messagePayload) throws IOException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy-hh:mm:ss");
        Date date = new Date();
        writer.write(dateFormat.format(date) + ": " + s + ": " + messagePayload);
        writer.newLine();
        writer.flush();
    }
}
