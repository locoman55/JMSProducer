JMSProducer
===========

Simple producer of JMS messages (Topics or Queues) that makes use of JMSChannel.

JMSProducer has dependencies on the JMSChannel and AvroDemo projects. The JMSConsumer project is a companion project and is needed to consume messages published by JMSConsumer. The messages can be text (the default) or they can be the User object that is serialized / deserialized using Avro and sent as a byte array (BytesMessage).
