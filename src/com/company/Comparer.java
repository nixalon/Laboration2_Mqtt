package com.company;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Comparer implements IMqttMessageListener {

    String subscribeTopic;
    String publishTopic;
    String content;
    int qos;
    String broker;
    String clientId;
    MqttClient client;

    public Comparer() {                                           //Skapar ny MQTT klient här
        subscribeTopic = "myLivingroom/mySquareMeter/myTemperature";
        publishTopic = "myLivingroom/mySquareMeter/myCompare";
        content = "Message from MqttPublishSample";
        qos = 2;
        broker = "tcp://broker.hivemq.com";
        clientId = "Alexandra och Soleimans fantastiska temperaturjämförelsemakapär";
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            client.connect(options);
            client.subscribe(subscribeTopic, this);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void main (String args[]){
        Comparer comparer = new Comparer();
        //subscriber.compareTemp();
    }

    private void compareTemp(int temperature){
        String compare = "";
        if (temperature < 22)
            compare = "+";
        else if (temperature > 22)
            compare = "-";
        postToMQ(compare);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        String message = new String(mqttMessage.getPayload());
        int temperature = Integer.parseInt(message);
        compareTemp(temperature);
    }

    private void postToMQ(String compare) {
        MqttMessage message = new MqttMessage(compare.getBytes());
        message.setQos(qos);
        try {
            client.publish(publishTopic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        System.out.println("Message published");
    }
}
