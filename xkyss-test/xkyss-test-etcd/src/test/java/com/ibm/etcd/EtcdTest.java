package com.ibm.etcd;

import com.google.protobuf.ByteString;
import com.ibm.etcd.api.PutResponse;
import com.ibm.etcd.api.RangeResponse;
import com.ibm.etcd.client.EtcdClient;
import com.ibm.etcd.client.KvStoreClient;
import com.ibm.etcd.client.kv.KvClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EtcdTest {

    @Test
    public void test_01() {
        KvStoreClient client = EtcdClient.forEndpoint("localhost", 2399).withPlainText().build();
        KvClient kvClient = client.getKvClient();

        ByteString key = ByteString.copyFromUtf8("/xkyss-test/01");
        ByteString value = ByteString.copyFromUtf8("aaaaa");
        PutResponse result = kvClient.put(key, value).sync();

        Assertions.assertNotNull(result);

        RangeResponse sync = kvClient.get(key).sync();
        Assertions.assertEquals(1, sync.getKvsCount());
    }
}
