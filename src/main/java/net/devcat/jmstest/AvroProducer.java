package net.devcat.jmstest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jms.*;
 
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.util.ByteBufferInputStream;
import org.apache.commons.io.IOUtils;

import net.devcat.jmschannel.JMSOutChannel;
import net.devcat.avro.model.User;

public class AvroProducer {
    private static final SpecificDatumWriter<User> avroUserWriter = 
        new SpecificDatumWriter<User>(User.SCHEMA$);
    private static final EncoderFactory encoderFactory = EncoderFactory.get();

    public AvroProducer() {

    }

    public void publish(JMSOutChannel outChannel, User user) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BinaryEncoder binaryEncoder = 
                encoderFactory.binaryEncoder(stream, null);
            avroUserWriter.write(user, binaryEncoder);
            binaryEncoder.flush();
            IOUtils.closeQuietly(stream);
            Message m = outChannel.getBytesMessage(stream.toByteArray());
            outChannel.sendMsg(m);
        } catch (IOException e) {
            throw new RuntimeException("Avro serialization failure", e);
        }
    }
}
