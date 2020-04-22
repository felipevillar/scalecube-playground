import io.scalecube.services.transport.api.DataCodec;

public class ListCodecs {
    public static void main(String[] args) {
        DataCodec.getAllContentTypes().forEach(System.out::println);
    }
}
