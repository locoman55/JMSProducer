package net.devcat.jmstest;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import net.devcat.jmschannel.ChannelType;
import net.devcat.jmschannel.JMSOutChannel;
import net.devcat.jmschannel.exceptions.JMSChannelException;

public class JMSProducer {

    JMSProducer() {
    }

    private static Connection getConnection(String url) {
        ActiveMQConnectionFactory connectionFactory = 
            new ActiveMQConnectionFactory(url);
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    private static void doUsage() {
        java.lang.System.out.println(
            "USAGE: --type <TOPIC|QUEUE> --name <name>\n" +
            "       --msg <message string> --count <message count>\n");
    }

    public static void main(String[] argv) {
        String name = null;
        String msg = null;
        int count = 1;
        ChannelType type = ChannelType.UNKNOWN;
        JMSOutChannel outChannel = null;
        Connection connection = null;
        TextMessage textMsg = null;

        if (argv.length >= 1) {
            for (int i = 0; i < argv.length; i++) {
                if (argv[i].equals("--help")) {
                    doUsage();
                    System.exit(0);
                } else if (argv[i].equals("--type")) {
                    i++;
                    try {
                        type = ChannelType.valueOf(argv[i].toUpperCase());
                    } catch (IllegalArgumentException e) {
                    	System.out.printf(
                            "Invalid Channel type (%s)\n", argv[i]);
                        System.exit(1);
                    }
                } else if (argv[i].equals("--name")) {
                    i++;
                    name = argv[i];
                } else if (argv[i].equals("--msg")) {
                    i++;
                    msg = argv[i];
                } else if (argv[i].equals("--count")) {
                	i++;
                    count = Integer.valueOf(argv[i]);
                } else {
                    System.out.printf(
                        "Invalid command argument (%s)\n", argv[i]);
                    System.exit(-1);
                }
            }
        }

        try {
            connection = getConnection(ActiveMQConnection.DEFAULT_BROKER_URL);
            outChannel = new JMSOutChannel(type, name, connection);
        } catch (JMSChannelException e) {
            System.out.printf("%s\n", e.getMessage());
            System.exit(-1);
        }

        for (int i = 0; i < count; i++) {
            textMsg = outChannel.getTextMessage(Integer.toString(i)+":"+msg);
            outChannel.sendMsg(textMsg);
        }
        System.exit(0);
    }
}
