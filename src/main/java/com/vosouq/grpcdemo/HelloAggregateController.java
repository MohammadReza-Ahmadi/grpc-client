package com.vosouq.grpcdemo;

import com.vosouq.gen.proto.GreeterGrpc;
import com.vosouq.gen.proto.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/agg")
public class HelloAggregateController {

    private final RestTemplate restTemplate;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    public HelloAggregateController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        final String target = "localhost:6565";
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();
        blockingStub = GreeterGrpc.newBlockingStub(channel);

    }

    @GetMapping(value = "/rest/{name}")
    public String callByRest(@PathVariable String name) {
        final String uri = "http://localhost:5555/" + name;
        return restTemplate.getForObject(uri, String.class);
    }

    @GetMapping(value = "/rest-list/{name}/{count}")
    public List<String> callByRestList(@PathVariable String name, @PathVariable int count) {
        final String uri = "http://localhost:5555/" + name;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String result = restTemplate.getForObject(uri, String.class);
            list.add(result);
        }
        return list;
    }

    @GetMapping(value = "/grpc/{name}")
    public String callByGrpc(@PathVariable String name) {
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        return blockingStub.sayHello(request).getReply();
    }

    @GetMapping(value = "/grpc-list/{name}/{count}")
    public List<String> callByGrpcList(@PathVariable String name, @PathVariable int count) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            HelloRequest request = HelloRequest.newBuilder().setName(name).build();
            list.add(blockingStub.sayHello(request).getReply());
        }
        return list;
    }
}
