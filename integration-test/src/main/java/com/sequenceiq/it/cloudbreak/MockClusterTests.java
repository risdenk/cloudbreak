package com.sequenceiq.it.cloudbreak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.CloudbreakTest;
import com.sequenceiq.it.cloudbreak.newway.ImageCatalog;
import com.sequenceiq.it.cloudbreak.newway.Mock;

public class MockClusterTests extends CloudbreakTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockClusterTests.class);

    private static final String VALID_IMAGECATALOG_URL = "https://localhost:9443/imagecatalog";
    private static final String VALID_IMAGECATALOG_NAME = "mock-image-catalog";

    @Test
    public void testSetNewDefaultImageCatalog() throws Exception {
        given(CloudbreakClient.isCreated());
        given(Mock.isCreated());
        given(ImageCatalog.request()
                .withName(VALID_IMAGECATALOG_NAME)
                .withUrl(VALID_IMAGECATALOG_URL), "an imagecatalog request and set as default"

        );
        when(ImageCatalog.post());
        when(ImageCatalog.setDefault());
        then(ImageCatalog.assertThis(
                (imageCatalog, t) -> {
                    Assert.assertEquals(imageCatalog.getResponse().getName(), VALID_IMAGECATALOG_NAME);
                    Assert.assertEquals(imageCatalog.getResponse().isUsedAsDefault(), true);
                }),  "check imagecatalog is created and set as default");
    }

//    @Test
//    public void testCreateNewRegularCluster() throws Exception {
//        CloudProvider cloudProvider = CloudProviderHelper.providerFactory(MockCloudProvider.MOCK, getTestParameter());
//        String blueprintName = "Data Science: Apache Spark 2, Apache Zeppelin";
//        String clusterName = "mockcluster";
//        given(Mock.isCreated());
//        given(CloudbreakClient.isCreated());
//        given(cloudProvider.aValidCredential());
//        given(Cluster.request()
//                        .withAmbariRequest(cloudProvider.ambariRequestWithBlueprintName(blueprintName)),
//                "a cluster request");
//        given(cloudProvider.aValidStackRequest()
//                .withName(clusterName), "a stack request");
//        when(Stack.post(), "post the stack request");
//    }

//
//    @Test(dataProvider = "providernameblueprintimage", priority = 10)
//    public void testCreateNewHdfCluster(CloudProvider cloudProvider, String clusterName, String blueprintName, String imageId) throws Exception {
//        given(CloudbreakClient.isCreated());
//        given(cloudProvider.aValidCredential());
//        given(Cluster.request()
//                        .withAmbariRequest(cloudProvider.ambariRequestWithBlueprintName(blueprintName)),
//                "a cluster request");
//        given(ImageSettings.request()
//                .withImageCatalog("")
//                .withImageId(imageId));
//        given(HostGroups.request()
//                .addHostGroup(cloudProvider.hostgroup("Services", InstanceGroupType.GATEWAY, 1))
//                .addHostGroup(cloudProvider.hostgroup("NiFi", InstanceGroupType.CORE, 1))
//                .addHostGroup(cloudProvider.hostgroup("ZooKeeper", InstanceGroupType.CORE, 1)));
//        given(cloudProvider.aValidStackRequest()
//                .withName(clusterName), "a stack request");
//        when(Stack.post(), "post the stack request");
//        then(Stack.waitAndCheckClusterAndStackAvailabilityStatus(),
//                "wait and check availability");
//        then(Stack.checkClusterHasAmbariRunning(
//                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_PORT),
//                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_USER),
//                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_PASSWORD)),
//                "check ambari is running and components available");
//    }
//
//    @Test(dataProvider = "providernameblueprintimageos", priority = 10)
//    public void testCreateNewClusterWithOs(CloudProvider cloudProvider, String clusterName, String blueprintName, String os, Kerberos kerberos)
//            throws Exception {
//        given(CloudbreakClient.isCreated());
//        given(cloudProvider.aValidCredential());
//        given(kerberos);
//        given(Cluster.request()
//                        .withAmbariRequest(cloudProvider.ambariRequestWithBlueprintName(blueprintName)),
//                "a cluster request");
//        given(ImageSettings.request()
//                .withImageCatalog("")
//                .withOs(os));
//        given(cloudProvider.aValidStackRequest()
//                .withName(clusterName), "a stack request");
//        when(Stack.post(), "post the stack request");
//        then(Stack.waitAndCheckClusterAndStackAvailabilityStatus(),
//                "wait and check availability");
//        then(Stack.checkClusterHasAmbariRunning(
//                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_PORT),
//                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_USER),
//                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_PASSWORD)),
//                "check ambari is running and components available");
//    }
//
//    @Test(dataProvider = "providernameblueprintimage", priority = 10)
//    public void testCreateNewClusterWithKnox(CloudProvider cloudProvider, String clusterName, String blueprintName, String imageId) throws Exception {
//        given(CloudbreakClient.isCreated());
//        given(cloudProvider.aValidCredential());
//        given(Cluster.request()
//                        .withAmbariRequest(cloudProvider.ambariRequestWithBlueprintName(blueprintName)),
//                "a cluster request");
//        given(ImageSettings.request()
//                .withImageCatalog("")
//                .withImageId(imageId));
//        given(ClusterGateway.request()
//                .withPath("test-gateway")
//                .withSsoType(SSOType.NONE)
//                .withTopology(GatewayTopology.request()
//                        .withName("test-topology")
//                        .withExposedServices(Collections.singletonList("ALL"))
//                )
//        );
//        given(cloudProvider.aValidStackRequest().withName(clusterName), "a stack request");
//
//        when(Stack.post(), "post the stack request");
//
//        then(Stack.waitAndCheckClusterAndStackAvailabilityStatus(),
//                "wait and check availability");
//        then(Stack.checkClusterHasAmbariRunningThroughKnox(
//                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_USER),
//                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_PASSWORD)),
//                "check if ambari is available through knox");
//    }
//
//    @Test(dataProvider = "providernamehostgroupdesiredno", priority = 20)
//    public void testScaleCluster(CloudProvider cloudProvider, String clusterName, String hostgroupName, int desiredCount) throws Exception {
//        given(CloudbreakClient.isCreated());
//        given(cloudProvider.aValidCredential());
//        given(cloudProvider.aValidStackIsCreated()
//                .withName(clusterName), "a stack is created");
//        given(StackOperation.request()
//                .withGroupName(hostgroupName)
//                .withDesiredCount(desiredCount), "a scale request to " + hostgroupName);
//        when(StackOperation.scale(), "scale");
//        when(Stack.get());
//        then(Stack.waitAndCheckClusterAndStackAvailabilityStatus(), "wait for availability");
//        then(Stack.checkClusterHasAmbariRunning(
//                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_PORT),
//                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_USER),
//                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_PASSWORD)),
//                "check ambari");
//    }
//
//    @Test(dataProvider = "providername", priority = 30)
//    public void testStopCluster(CloudProvider cloudProvider, String clusterName) throws Exception {
//        given(CloudbreakClient.isCreated());
//        given(cloudProvider.aValidCredential());
//        given(cloudProvider.aValidStackIsCreated()
//                .withName(clusterName), "a stack is created");
//        given(StackOperation.request());
//        when(StackOperation.stop());
//        when(Stack.get());
//        then(Stack.waitAndCheckClusterAndStackStoppedStatus(), "stack has been stopped");
//    }
//
//    @Test(dataProvider = "providername", priority = 40)
//    public void testStartCluster(CloudProvider cloudProvider, String clusterName) throws Exception {
//        given(CloudbreakClient.isCreated());
//        given(cloudProvider.aValidCredential());
//        given(cloudProvider.aValidStackIsCreated()
//                .withName(clusterName), "a stack is created");
//        given(StackOperation.request());
//        when(StackOperation.start());
//        when(Stack.get());
//        then(Stack.waitAndCheckClusterAndStackAvailabilityStatus(), "stack has been started");
//        then(Stack.checkClusterHasAmbariRunning(
//                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_PORT),
//                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_USER),
//                getTestParameter().get(CloudProviderHelper.DEFAULT_AMBARI_PASSWORD)),
//                "ambari check");
//    }
//
//    @Test(alwaysRun = true, dataProvider = "providername", priority = 50)
//    public void testTerminateCluster(CloudProvider cloudProvider, String clusterName) throws Exception {
//        given(CloudbreakClient.isCreated());
//        given(cloudProvider.aValidCredential());
//        given(cloudProvider.aValidStackIsCreated()
//                .withName(clusterName), "a stack is created");
//        when(Stack.delete());
//        then(Stack.waitAndCheckClusterDeleted(), "stack has been deleted");
//    }
//
//    @DataProvider(name = "providernameblueprintimage")
//    public Object[][] providerAndImage() throws Exception {
//        String blueprint = getTestParameter().get("blueprintName");
//        String provider = getTestParameter().get("provider").toLowerCase();
//        String imageDescription = getTestParameter().get("image");
//        CloudProvider cloudProvider = CloudProviderHelper.providerFactory(provider, getTestParameter());
//        //String imageCatalog = getTestParameter().get("imageCatalog");
//        String clusterName = getTestParameter().get("clusterName");
//        String image = getImageId(provider, imageDescription);
//        return new Object[][]{
//                {cloudProvider, clusterName, blueprint, image}
//        };
//    }
//
//    @DataProvider(name = "providernameblueprintimageos")
//    public Object[][] providerAndImageOs() {
//        String blueprint = getTestParameter().get("blueprintName");
//        String provider = getTestParameter().get("provider").toLowerCase();
//        String imageOs = getTestParameter().get("imageos");
//        CloudProvider cloudProvider = CloudProviderHelper.providerFactory(provider, getTestParameter());
//        String clusterName = getTestParameter().get("clusterName");
//        Kerberos kerberos = Kerberos.request()
//                .withMasterKey(Kerberos.DEFAULT_MASTERKEY)
//                .withAdmin(Kerberos.DEFAULT_ADMIN_USER)
//                .withPassword(Kerberos.DEFAULT_ADMIN_PASSWORD);
//        return new Object[][]{
//                {cloudProvider, clusterName, blueprint, imageOs, kerberos}
//        };
//    }
//
//    @DataProvider(name = "providernamehostgroupdesiredno")
//    public Object[][] providerAndHostgroup() throws Exception {
//        String hostgroupName = getTestParameter().get("instancegroupName");
//        String provider = getTestParameter().get("provider").toLowerCase();
//        CloudProvider cloudProvider = CloudProviderHelper.providerFactory(provider, getTestParameter());
//        String clusterName = getTestParameter().get("clusterName");
//        return new Object[][]{
//                {cloudProvider, clusterName, hostgroupName, DESIRED_NO}
//        };
//    }
//
//    @DataProvider(name = "providername")
//    public Object[][] providerClusterName() throws Exception {
//        String provider = getTestParameter().get("provider").toLowerCase();
//        CloudProvider cloudProvider = CloudProviderHelper.providerFactory(provider, getTestParameter());
//        String clusterName = getTestParameter().get("clusterName");
//        return new Object[][]{
//                {cloudProvider, clusterName}
//        };
//    }
//
//    private String getImageId(String provider, String imageDescription) throws Exception {
//        given(CloudbreakClient.isCreated());
//        CloudbreakClient clientContext = CloudbreakClient.getTestContextCloudbreakClient().apply(getItContext());
//        com.sequenceiq.cloudbreak.client.CloudbreakClient client = clientContext.getCloudbreakClient();
//        ImagesResponse imagesByProvider = client.imageCatalogEndpoint().getImagesByProvider(provider);
//        switch (imageDescription) {
//            case "hdf":
//                return getLastUuid(imagesByProvider.getHdfImages());
//            case "hdp":
//                return getLastUuid(imagesByProvider.getHdpImages());
//            default:
//                return getLastUuid(imagesByProvider.getBaseImages());
//        }
//    }
//
//    private String getLastUuid(List<? extends ImageResponse> images) {
//        List<? extends ImageResponse> result = images.stream().filter(ImageResponse::isDefaultImage).collect(Collectors.toList());
//        if (result.isEmpty()) {
//            result = images;
//        }
//        result = result.stream().sorted(Comparator.comparing(ImageResponse::getDate)).collect(Collectors.toList());
//        return result.get(result.size() - 1).getUuid();
//    }
}
