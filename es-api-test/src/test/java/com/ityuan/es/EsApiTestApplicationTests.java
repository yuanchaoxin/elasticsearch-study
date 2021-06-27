package com.ityuan.es;

import com.alibaba.fastjson.JSON;
import com.ityuan.es.bean.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class EsApiTestApplicationTests {

    @Resource
    private RestHighLevelClient client;

    /**
     * 创建索引
     *
     * @throws IOException
     */
    @Test
    public void testCreateIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("yuan_index");
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    /**
     * 索引是否存在、获取索引
     *
     * @throws IOException
     */
    @Test
    public void testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("yuan_index");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        if (exists) {
            GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);
            System.out.println(response.getIndices());
        }
    }

    /**
     * 删除索引
     *
     * @throws IOException
     */
    @Test
    public void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("helloword");
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    /**
     * 创建文档
     *
     * @throws IOException
     */
    @Test
    public void testCreateDocument() throws IOException {
        User user = new User("yuan", 1);
        IndexRequest request = new IndexRequest("yuan_index");
        request.timeout("1s");
        request.id("2");
        request.source(JSON.toJSONString(user), XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    /**
     * 文档是否存在
     *
     * @throws IOException
     */
    @Test
    public void testExistDoc() throws IOException {
        GetRequest request = new GetRequest("yuan_index", "2");
        request.fetchSourceContext(new FetchSourceContext(false));
        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 获取文档
     *
     * @throws IOException
     */
    @Test
    public void testGetDocument() throws IOException {
        GetRequest request = new GetRequest("yuan_index", "1");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSource());
    }

    /**
     * 更新文档
     *
     * @throws IOException
     */
    @Test
    public void testUpdateDocument() throws IOException {
        UpdateRequest request = new UpdateRequest("yuan_index", "1");
        User user = new User("update", 1);
        request.timeout("1s");
        request.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    /**
     * 删除文档
     *
     * @throws IOException
     */
    @Test
    public void testDeleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("yuan_index", "2");
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    /**
     * 批量插入文档
     *
     * @throws IOException
     */
    @Test
    public void testBulkAddDocument() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");

        List<User> userList = Arrays.asList(
                new User("张三1", 1),
                new User("张三2", 1),
                new User("张三3", 1),
                new User("张三4", 1),
                new User("张三5", 1),
                new User("张三6", 1)
        );

        for (int i = 0; i < userList.size(); i++) {
            IndexRequest request = new IndexRequest("yuan_index");
            request.source(JSON.toJSONString(userList.get(i)), XContentType.JSON);
            request.id("" + i + 1);
            bulkRequest.add(request);
        }
        BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(response.hasFailures());
    }

    /**
     * 批量删除文档
     */
    @Test
    public void testBulkDeleteDocument() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        List<String> list = Arrays.asList("01", "11", "21", "31");

        for (String id : list) {
            bulkRequest.add(new DeleteRequest("yuan_index", id));
        }

        BulkResponse responses = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(responses.hasFailures());
    }

    /**
     * 条件搜索文档
     *
     * @throws IOException
     */
    @Test
    public void testSearchDocument() throws IOException {
        SearchRequest searchRequest = new SearchRequest("yuan_index");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.timeout(new TimeValue(1000, TimeUnit.SECONDS));
        searchSourceBuilder.query(matchAllQueryBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(response);
        for (SearchHit documentFields : response.getHits().getHits()) {
            System.out.println(documentFields.getSourceAsString());
        }
    }

    @Test
    void contextLoads() {
    }

}
