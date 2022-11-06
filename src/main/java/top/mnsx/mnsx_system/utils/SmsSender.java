package top.mnsx.mnsx_system.utils;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/11/5 15:34
 * @Description: 短信服务
 */
public class SmsSender {
    //产品域名,开发者无需替换
    private static final String domain = "dysmsapi.aliyuncs.com";                // 无需修改


    // 阿里云配置
    private static final String accessKeyId = "";
    private static final String accessKeySecret = "";
    // 签名和模板
    private static final String signName = "mnsx";
    private static final String templateCode = "SMS_254825609";
    // 地域
    private static final String providerRegionID = "cn-chengdu";

    public static void sendSms(String phoneNumbers, String templateParam) {

        // 操作参数
        templateParam = "{\"code\":\"" + templateParam + "\"}";

        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKeyId)
                .accessKeySecret(accessKeySecret)
                .build());

        AsyncClient client = AsyncClient.builder()
                .region(providerRegionID) // Region ID
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride(domain)
                )
                .build();

        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName(signName)
                .templateCode(templateCode)
                .phoneNumbers(phoneNumbers)
                .templateParam(templateParam)
                .build();

        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        SendSmsResponse resp = null;
        try {
            resp = response.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(new Gson().toJson(resp));

        client.close();
    }
}
