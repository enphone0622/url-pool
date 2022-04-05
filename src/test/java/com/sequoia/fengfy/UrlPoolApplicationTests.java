package com.sequoia.fengfy;

import com.sequoia.fengfy.cache.CacheContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
class UrlPoolApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private CacheContainer cacheContainer;

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }


    /**
     * 正例测试，用一个正确的真实的URL进行测试
     * 期望: 返回短地址
     * @throws Exception
     */
    @Test
    public void testGenerateShortUrl1() throws Exception {
        String  nativeUrl = "http://dict.youdao.com/";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/link/short")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("nativeUrl",nativeUrl))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("返回结果：" + result);
    }


    /**
     * 反例测试，用一个错误的URL地址进行测试
     * 期望: 返回相应的错误提醒信息
     * @throws Exception
     */
    @Test
    public void testGenerateShortUrl2() throws Exception {
        String nativeUrl = "dict.youdao.com";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/link/short")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("nativeUrl",nativeUrl))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("返回结果：" + result);
    }

    /**
     * 正例测试，用一个正确的短地址进行测试
     * 期望: 返回还原的原生地址信息
     * @throws Exception
     */
    @Test
    public void testParseToNativeUrl1() throws Exception {
        String shortUrl = "http://f.com/V6dia3";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/link/native")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("shortUrl",shortUrl))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("返回结果：" + result);
    }

    /**
     * 反例测试，用一个不存在的的短地址进行测试
     * 期望: 返回相应的错误提醒信息
     * @throws Exception
     */
    @Test
    public void testParseToNativeUrl2() throws Exception {
        String shortUrl = "http://f.com/112233";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/link/native")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("shortUrl",shortUrl))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        System.out.println("返回结果：" + result);
    }


    /**
     * 测试大数据量下的缓存处理情况，是否会内存溢出
     * 模拟场景：
     * @throws Exception
     */
    @Test
    public void testCache() throws Exception {

        System.out.println("==========================100w读写性能测试开始========================");
        String domain = "http://f.com";
        //创建有10个线程的线程池，将1000000次操作分10次添加到线程池
        int threads = 10;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        //每批操作数量
        int batchSize = 100000;

        //添加
        {
            CountDownLatch latch = new CountDownLatch(threads);
            AtomicInteger n = new AtomicInteger(0);
            long start = System.currentTimeMillis();

            for (int t = 0; t < threads; t++) {
                pool.submit(() -> {
                    for (int i = 0; i < batchSize; i++) {
                        int value = n.incrementAndGet();
                        cacheContainer.put(domain.concat("/").concat(String.valueOf(value)), value, 300000);
                    }
                    latch.countDown();
                });
            }
            //等待全部线程执行完成，打印执行时间
            latch.await();
            System.out.printf("添加耗时：%dms\n", System.currentTimeMillis() - start);
        }

        //查询
        {
            CountDownLatch latch = new CountDownLatch(threads);
            AtomicInteger n = new AtomicInteger(0);
            long start = System.currentTimeMillis();
            for (int t = 0; t < threads; t++) {
                pool.submit(() -> {
                    for (int i = 0; i < batchSize; i++) {
                        int value = n.incrementAndGet();
                        cacheContainer.get(domain.concat("/").concat(String.valueOf(value)));
                    }
                    latch.countDown();
                });
            }
            //等待全部线程执行完成，打印执行时间
            latch.await();
            System.out.printf("查询耗时：%dms\n", System.currentTimeMillis() - start);
        }

        System.out.println("当前缓存容量：" + cacheContainer.size());
        System.out.println("==========================100w读写性能测试结束========================");
    }


}
