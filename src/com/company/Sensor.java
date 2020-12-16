package com.company;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;

public class Sensor {
    String topic;
    String content;
    int qos;
    String broker;
    String clientId;
    MqttClient client;

    public Sensor() {                                           //Skapa ny MQTT klient här
        topic = "myLivingroom/mySquareMeter/myTemperature";
        content = "Message from MqttPublishSample";
        qos = 2;
        broker = "tcp://broker.hivemq.com";
        clientId = "Alexandra och Soleimans fantastiska temperaturskaparmakapär";
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            client = new MqttClient(broker, clientId, persistence);
            client.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public int generateRandomTemperature() {
        Random rand = new Random();
        int randomTemperature = rand.nextInt(25 + 1 - 15) + 15;
        return randomTemperature;
    }

    public void measureEveryMinute() {
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            postToMQ(generateRandomTemperature());
        }
    }

    private void postToMQ(int randomTemperature) {
        MqttMessage message = new MqttMessage(Integer.toString(randomTemperature).getBytes());
        message.setQos(qos);
        try {
            client.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        System.out.println("Message published");
    }

    public static void main (String args[]){
        Sensor sensor = new Sensor();
        sensor.measureEveryMinute();
    }
}


