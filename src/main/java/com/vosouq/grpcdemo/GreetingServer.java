package com.vosouq.grpcdemo;

import com.vosouq.gen.proto.GreeterGrpc.GreeterImplBase;
import com.vosouq.gen.proto.HelloReply;
import com.vosouq.gen.proto.HelloRequest;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@GRpcService
public class GreetingServer extends GreeterImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingServer.class);

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        LOGGER.info("server received {}", request);
        String message = "Hello " + request.getName() +"!";
        HelloReply helloReply = HelloReply.newBuilder().setReply(message).build();
        LOGGER.info("server responded {}", helloReply);
        responseObserver.onNext(helloReply);
        responseObserver.onCompleted();
    }

}