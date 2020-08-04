package client;

import com.order.generated.stubs.OrderServiceGrpc;
import com.order.generated.stubs.UserPurchaseHistory;
import com.order.generated.stubs.UserPurchaseHistoryRequest;
import com.order.generated.stubs.UserPurchaseHistoryResponse;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PipelineMain {

    public static void main(String[] args) {

        ManagedChannel channel=OrderClient.getChannelInstance();
        OrderServiceGrpc.OrderServiceBlockingStub stub= OrderServiceGrpc.newBlockingStub(channel);
        System.out.println("Sending request to server");
        try {
            UserPurchaseHistoryResponse response=stub.getUserPurchaseHistory(UserPurchaseHistoryRequest.newBuilder().setAccountId("AC001").build());
            List<UserPurchaseHistory> uph= new ArrayList<>();
            uph= response.getUserPurcHistList();
            System.out.println("Successfully captured the response");
            applyTransform(uph);
        }catch (StatusRuntimeException exception){
            System.out.println("Exception occurred while getting the response from server, reason: "+ exception.getMessage());
            //System.out.println(exception.getCause());
        }catch (Exception ex){
            System.out.println("Exception occurred "+ex.getMessage());
        }
    }

    public static  void applyTransform(List<UserPurchaseHistory> uph) throws IOException {

        Pipeline pipeline = Pipeline.create(PipelineOptionsFactory.fromArgs().withValidation().create());
        //PCollection<Orders> ordersCol=pipeline.apply(Create.of(orders));
        System.out.println("response="+uph);

        /*ObjectMapper mapper=new ObjectMapper();
        String json=mapper.writeValueAsString(orders);

        PCollection<String> stringResponse= pipeline.apply(Create.of(json));
        stringResponse.apply(TextIO.write().to("./src/main/output/objectValue.txt"));*/
    }
}
