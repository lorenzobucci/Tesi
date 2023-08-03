package io.github.lorenzobucci.tesi.metamodel.e2e_test;

import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import com.google.protobuf.util.Timestamps;
import io.github.lorenzobucci.tesi.metamodel.mobile_device.service.gRPC.MobileDeviceContract;
import io.github.lorenzobucci.tesi.metamodel.resources_management.service.gRPC.ResourcesManagementContract;
import io.github.lorenzobucci.tesi.metamodel.services_management.service.gRPC.ServicesManagementContract;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class E2ETest {

    private static final MobileDeviceClient mobileDeviceClient = new MobileDeviceClient();
    private static final ServicesManagementClient servicesManagementClient = new ServicesManagementClient();
    private static final ResourcesManagementClient resourcesManagementClient = new ResourcesManagementClient();

    private static final ArrayList<Long> mobileDevices = new ArrayList<>();

    private static final ArrayList<Long> endPointServiceTypes = new ArrayList<>();
    private static final ArrayList<Long> serviceTypes = new ArrayList<>();
    private static final ArrayList<Long> workflowTypes = new ArrayList<>();

    private static final ArrayList<Long> containerTypes = new ArrayList<>();
    private static final ArrayList<Long> nodes = new ArrayList<>();

    @BeforeAll
    public static void setup() {

        // MOBILE DEVICE

        mobileDevices.add(mobileDeviceClient.getCrudBlockingStub().addMobileDeviceDT(
                MobileDeviceContract.MobileDeviceDTConstructorParameters.newBuilder().build()).getId());

        mobileDeviceClient.getOperationalBlockingStub().syncMobileDeviceDTProperties(
                MobileDeviceContract.SyncMobileDeviceDTParameters.newBuilder()
                        .setMobileDeviceDTId(mobileDevices.get(0))
                        .setCurrentPosition(MobileDeviceContract.PositionDTO.newBuilder()
                                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                                .setLatitude(43.1726f)
                                .setLongitude(11.6583f)
                                .build())
                        .build());

        mobileDeviceClient.getOperationalBlockingStub().syncMobileDeviceDTProperties(
                MobileDeviceContract.SyncMobileDeviceDTParameters.newBuilder()
                        .setMobileDeviceDTId(mobileDevices.get(0))
                        .setCurrentPosition(MobileDeviceContract.PositionDTO.newBuilder()
                                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                                .setLatitude(43.1839f)
                                .setLongitude(11.6024f)
                                .build())
                        .build());

        // SERVICES MANAGEMENT

        endPointServiceTypes.add(servicesManagementClient.getCrudBlockingStub().addEndpointServiceType(
                ServicesManagementContract.EndpointServiceTypeConstructorParameters.newBuilder()
                        .setServiceName("endpoint1")
                        .setPhysicalEndpointURI("example.com/doSomething")
                        .setServiceRequirements(ServicesManagementContract.ServiceRequirementsDTO.newBuilder().build())
                        .build()).getId());
        endPointServiceTypes.add(servicesManagementClient.getCrudBlockingStub().addEndpointServiceType(
                ServicesManagementContract.EndpointServiceTypeConstructorParameters.newBuilder()
                        .setServiceName("endpoint2")
                        .setPhysicalEndpointURI("example.com/doSomethingElse")
                        .setServiceRequirements(ServicesManagementContract.ServiceRequirementsDTO.newBuilder().build())
                        .build()).getId());

        for (int i = 0; i < 5; i++)
            serviceTypes.add(servicesManagementClient.getCrudBlockingStub().addServiceType(
                    ServicesManagementContract.ServiceTypeConstructorParameters.newBuilder()
                            .setServiceName("service" + i)
                            .setServiceRequirements(ServicesManagementContract.ServiceRequirementsDTO.newBuilder().build())
                            .build()).getId());

        workflowTypes.add(servicesManagementClient.getCrudBlockingStub().addWorkflowType(
                ServicesManagementContract.WorkflowTypeConstructorParameters.newBuilder()
                        .setEndpointServiceTypeId(endPointServiceTypes.get(0))
                        .build()).getId());

        servicesManagementClient.getOperationalBlockingStub().addServiceTypeToWorkflowType(
                ServicesManagementContract.AddServiceTypeToWorkflowTypeParameters.newBuilder()
                        .setWorkflowTypeId(workflowTypes.get(0))
                        .setServiceTypeToAddId(serviceTypes.get(0))
                        .setCallerServiceTypeId(endPointServiceTypes.get(0))
                        .build());
        servicesManagementClient.getOperationalBlockingStub().addServiceTypeToWorkflowType(
                ServicesManagementContract.AddServiceTypeToWorkflowTypeParameters.newBuilder()
                        .setWorkflowTypeId(workflowTypes.get(0))
                        .setServiceTypeToAddId(serviceTypes.get(1))
                        .setCallerServiceTypeId(endPointServiceTypes.get(0))
                        .build());
        servicesManagementClient.getOperationalBlockingStub().addServiceTypeToWorkflowType(
                ServicesManagementContract.AddServiceTypeToWorkflowTypeParameters.newBuilder()
                        .setWorkflowTypeId(workflowTypes.get(0))
                        .setServiceTypeToAddId(serviceTypes.get(2))
                        .setCallerServiceTypeId(endPointServiceTypes.get(0))
                        .build());
        servicesManagementClient.getOperationalBlockingStub().addServiceTypeToWorkflowType(
                ServicesManagementContract.AddServiceTypeToWorkflowTypeParameters.newBuilder()
                        .setWorkflowTypeId(workflowTypes.get(0))
                        .setServiceTypeToAddId(serviceTypes.get(3))
                        .setCallerServiceTypeId(serviceTypes.get(2))
                        .build());
        servicesManagementClient.getOperationalBlockingStub().addServiceTypeToWorkflowType(
                ServicesManagementContract.AddServiceTypeToWorkflowTypeParameters.newBuilder()
                        .setWorkflowTypeId(workflowTypes.get(0))
                        .setServiceTypeToAddId(serviceTypes.get(4))
                        .setCallerServiceTypeId(serviceTypes.get(0))
                        .build());
        servicesManagementClient.getOperationalBlockingStub().addRPCToWorkflowType(
                ServicesManagementContract.AddRPCToWorkflowTypeParameters.newBuilder()
                        .setWorkflowTypeId(workflowTypes.get(0))
                        .setCallerServiceTypeId(serviceTypes.get(2))
                        .setCalleeServiceTypeId(serviceTypes.get(1))
                        .build());

        // RESOURCES MANAGEMENT

        containerTypes.add(resourcesManagementClient.getCrudBlockingStub().addContainerType(
                ResourcesManagementContract.ContainerTypeConstructorParameters.newBuilder()
                        .setImageName("Ubuntu")
                        .setImageVersion("22.04")
                        .build()).getId());
        containerTypes.add(resourcesManagementClient.getCrudBlockingStub().addContainerType(
                ResourcesManagementContract.ContainerTypeConstructorParameters.newBuilder()
                        .setImageName("Ubuntu")
                        .setImageVersion("20.04")
                        .build()).getId());

        nodes.add(resourcesManagementClient.getCrudBlockingStub().addNode(
                ResourcesManagementContract.NodeConstructorParameters.newBuilder()
                        .setLatitude(14.3648f)
                        .setLongitude(36.3792f)
                        .setIpAddress("10.10.10.1")
                        .setNodeType(ResourcesManagementContract.NodeTypeDTO.EDGE)
                        .setProperties(ResourcesManagementContract.NodeTechnicalPropertiesDTO.newBuilder()
                                .setCpuCoresNumber(6)
                                .setCpuFrequencyGHz(3.20f)
                                .setInstalledOS("Ubuntu")
                                .setMemoryMB(1024)
                                .build())
                        .build()).getId());
        nodes.add(resourcesManagementClient.getCrudBlockingStub().addNode(
                ResourcesManagementContract.NodeConstructorParameters.newBuilder()
                        .setLatitude(-67.3612f)
                        .setLongitude(6.7906f)
                        .setIpAddress("10.10.10.2")
                        .setNodeType(ResourcesManagementContract.NodeTypeDTO.CLOUD)
                        .setProperties(ResourcesManagementContract.NodeTechnicalPropertiesDTO.newBuilder()
                                .setCpuCoresNumber(12)
                                .setCpuFrequencyGHz(3.40f)
                                .setInstalledOS("FreeBSD")
                                .setMemoryMB(8192)
                                .build())
                        .build()).getId());

        resourcesManagementClient.getOperationalBlockingStub().syncNodeDTProperties(
                ResourcesManagementContract.NodeDTSyncParameters.newBuilder()
                        .setNodeId(nodes.get(0))
                        .setCpuUsagePercentage(0.14f)
                        .setMemoryUsagePercentage(0.09f)
                        .build());
        resourcesManagementClient.getOperationalBlockingStub().syncNodeDTProperties(
                ResourcesManagementContract.NodeDTSyncParameters.newBuilder()
                        .setNodeId(nodes.get(1))
                        .setCpuUsagePercentage(0.74f)
                        .setMemoryUsagePercentage(0.35f)
                        .build());
    }

    @Test
    public void testContainerAllocationOnSignalMobileDeviceEndpointInvocation() {
        mobileDeviceClient.getOperationalBlockingStub().signalMobileDeviceEndpointInvocation(
                MobileDeviceContract.EndpointInvocationParameters.newBuilder()
                        .setMobileDeviceDTId(mobileDevices.get(0))
                        .setInvokedEndpointURI("example.com/doSomething")
                        .setParameters("someParameters")
                        .build());

        MobileDeviceContract.MobileDeviceDTDTO mobileDeviceDTDTO = mobileDeviceClient.getCrudBlockingStub().getMobileDeviceDT(Int64Value.of(mobileDevices.get(0)));
        MobileDeviceContract.TaskDTO taskDTO = mobileDeviceClient.getCrudBlockingStub().getTask(Int64Value.of(mobileDeviceDTDTO.getRunningTasksId(0)));
        assertThat(taskDTO.getAssociatedWorkflowId()).isNotZero();

        List<ServicesManagementContract.WorkflowInstanceDTO> workflowInstances =
                servicesManagementClient.getCrudBlockingStub().retrieveWorkflowInstances(Empty.newBuilder().build()).getWorkflowInstancesList();
        assertThat(workflowInstances.size()).isEqualTo(1);

        ServicesManagementContract.EndpointServiceInstanceDTO endpointServiceInstance =
                servicesManagementClient.getCrudBlockingStub().getEndpointServiceInstance(Int64Value.of(workflowInstances.get(0).getEndpointServiceInstanceId()));
        assertThat(endpointServiceInstance.getParameters()).isEqualTo("someParameters");

        ServicesManagementContract.EndpointServiceTypeDTO endpointServiceType =
                servicesManagementClient.getCrudBlockingStub().getEndpointServiceType(Int64Value.of(endpointServiceInstance.getEndpointServiceTypeId()));
        assertThat(endpointServiceType.getPhysicalEndpointURI()).isEqualTo("example.com/doSomething");

        List<ResourcesManagementContract.ContainerInstanceDTO> containerInstances =
                resourcesManagementClient.getCrudBlockingStub().retrieveContainerInstances(Empty.newBuilder().build()).getContainerInstancesList();
        assertThat(containerInstances.size()).isEqualTo(6);
    }

    @Test
    public void testContainersAreEquallyDistributedAmongTwoNodesWithSampleAllocatorAlgorithmWhenTaskOptimizationIsRequired() {
        mobileDeviceClient.getOperationalBlockingStub().signalMobileDeviceEndpointInvocation(
                MobileDeviceContract.EndpointInvocationParameters.newBuilder()
                        .setMobileDeviceDTId(mobileDevices.get(0))
                        .setInvokedEndpointURI("example.com/doSomething")
                        .setParameters("someParameters")
                        .build());

        StringValue previousAllocatorAlgorithm = resourcesManagementClient.getCrudBlockingStub().getCurrentAllocator(Empty.newBuilder().build());
        resourcesManagementClient.getCrudBlockingStub().setAllocatorAlgorithm(StringValue.of("io.github.lorenzobucci.tesi.metamodel.resources_management.allocator.SampleAllocatorAlgorithm"));

        mobileDeviceClient.getOperationalBlockingStub().requestMobileDeviceDTTaskOptimization(
                MobileDeviceContract.MobileDeviceDTTaskEndpoint.newBuilder().setMobileDeviceDTId(mobileDevices.get(0)).setTaskEndpointURI("example.com/doSomething").build());

        await().atMost(5, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS).untilAsserted(() -> {
            List<ResourcesManagementContract.NodeDTO> nodeList = resourcesManagementClient.getCrudBlockingStub().retrieveNodes(Empty.newBuilder().build()).getNodesList();
            assertThat(nodeList.get(0).getOwnedContainerInstancesIdCount()).isEqualTo(nodeList.get(1).getOwnedContainerInstancesIdCount());
        });

        resourcesManagementClient.getCrudBlockingStub().setAllocatorAlgorithm(previousAllocatorAlgorithm);
    }

    @Test
    public void testContainersAreEquallyDistributedAmongTwoNodesWithSampleAllocatorAlgorithmWhenMoreThan10MobileDeviceDTSyncAreCalled() {
        mobileDeviceClient.getOperationalBlockingStub().signalMobileDeviceEndpointInvocation(
                MobileDeviceContract.EndpointInvocationParameters.newBuilder()
                        .setMobileDeviceDTId(mobileDevices.get(0))
                        .setInvokedEndpointURI("example.com/doSomething")
                        .setParameters("someParameters")
                        .build());

        StringValue previousAllocatorAlgorithm = resourcesManagementClient.getCrudBlockingStub().getCurrentAllocator(Empty.newBuilder().build());
        resourcesManagementClient.getCrudBlockingStub().setAllocatorAlgorithm(StringValue.of("io.github.lorenzobucci.tesi.metamodel.resources_management.allocator.SampleAllocatorAlgorithm"));

        mobileDeviceClient.getOperationalBlockingStub().syncMobileDeviceDTProperties( // 3RD
                MobileDeviceContract.SyncMobileDeviceDTParameters.newBuilder()
                        .setMobileDeviceDTId(mobileDevices.get(0))
                        .setCurrentPosition(MobileDeviceContract.PositionDTO.newBuilder()
                                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                                .setLatitude(43.1319f)
                                .setLongitude(11.6784f)
                                .build())
                        .build());
        mobileDeviceClient.getOperationalBlockingStub().syncMobileDeviceDTProperties( // 4TH
                MobileDeviceContract.SyncMobileDeviceDTParameters.newBuilder()
                        .setMobileDeviceDTId(mobileDevices.get(0))
                        .setCurrentPosition(MobileDeviceContract.PositionDTO.newBuilder()
                                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                                .setLatitude(43.9659f)
                                .setLongitude(11.0424f)
                                .build())
                        .build());
        mobileDeviceClient.getOperationalBlockingStub().syncMobileDeviceDTProperties( // 5TH
                MobileDeviceContract.SyncMobileDeviceDTParameters.newBuilder()
                        .setMobileDeviceDTId(mobileDevices.get(0))
                        .setCurrentPosition(MobileDeviceContract.PositionDTO.newBuilder()
                                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                                .setLatitude(43.7049f)
                                .setLongitude(11.3824f)
                                .build())
                        .build());
        mobileDeviceClient.getOperationalBlockingStub().syncMobileDeviceDTProperties( // 6TH
                MobileDeviceContract.SyncMobileDeviceDTParameters.newBuilder()
                        .setMobileDeviceDTId(mobileDevices.get(0))
                        .setCurrentPosition(MobileDeviceContract.PositionDTO.newBuilder()
                                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                                .setLatitude(43.5603f)
                                .setLongitude(11.4607f)
                                .build())
                        .build());
        mobileDeviceClient.getOperationalBlockingStub().syncMobileDeviceDTProperties( // 7TH
                MobileDeviceContract.SyncMobileDeviceDTParameters.newBuilder()
                        .setMobileDeviceDTId(mobileDevices.get(0))
                        .setCurrentPosition(MobileDeviceContract.PositionDTO.newBuilder()
                                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                                .setLatitude(43.7604f)
                                .setLongitude(11.3470f)
                                .build())
                        .build());
        mobileDeviceClient.getOperationalBlockingStub().syncMobileDeviceDTProperties( // 8TH
                MobileDeviceContract.SyncMobileDeviceDTParameters.newBuilder()
                        .setMobileDeviceDTId(mobileDevices.get(0))
                        .setCurrentPosition(MobileDeviceContract.PositionDTO.newBuilder()
                                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                                .setLatitude(43.7567f)
                                .setLongitude(11.0343f)
                                .build())
                        .build());
        mobileDeviceClient.getOperationalBlockingStub().syncMobileDeviceDTProperties( // 9TH
                MobileDeviceContract.SyncMobileDeviceDTParameters.newBuilder()
                        .setMobileDeviceDTId(mobileDevices.get(0))
                        .setCurrentPosition(MobileDeviceContract.PositionDTO.newBuilder()
                                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                                .setLatitude(43.6707f)
                                .setLongitude(11.6044f)
                                .build())
                        .build());
        mobileDeviceClient.getOperationalBlockingStub().syncMobileDeviceDTProperties( //10 TH
                MobileDeviceContract.SyncMobileDeviceDTParameters.newBuilder()
                        .setMobileDeviceDTId(mobileDevices.get(0))
                        .setCurrentPosition(MobileDeviceContract.PositionDTO.newBuilder()
                                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                                .setLatitude(43.7808f)
                                .setLongitude(11.4600f)
                                .build())
                        .build());
        mobileDeviceClient.getOperationalBlockingStub().syncMobileDeviceDTProperties( //11 TH
                MobileDeviceContract.SyncMobileDeviceDTParameters.newBuilder()
                        .setMobileDeviceDTId(mobileDevices.get(0))
                        .setCurrentPosition(MobileDeviceContract.PositionDTO.newBuilder()
                                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                                .setLatitude(43.7804f)
                                .setLongitude(11.7604f)
                                .build())
                        .build());

        await().atMost(5, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS).untilAsserted(() -> {
            List<ResourcesManagementContract.NodeDTO> nodeList = resourcesManagementClient.getCrudBlockingStub().retrieveNodes(Empty.newBuilder().build()).getNodesList();
            assertThat(nodeList.get(0).getOwnedContainerInstancesIdCount()).isEqualTo(nodeList.get(1).getOwnedContainerInstancesIdCount());
        });

        resourcesManagementClient.getCrudBlockingStub().setAllocatorAlgorithm(previousAllocatorAlgorithm);
    }

    @Test
    public void testTaskAndContainerAndWorkflowInstancesAreDeletedOnMobileDeviceTaskCompletion() {
        mobileDeviceClient.getOperationalBlockingStub().signalMobileDeviceEndpointInvocation(
                MobileDeviceContract.EndpointInvocationParameters.newBuilder()
                        .setMobileDeviceDTId(mobileDevices.get(0))
                        .setInvokedEndpointURI("example.com/doSomething")
                        .setParameters("someParameters")
                        .build());

        mobileDeviceClient.getOperationalBlockingStub().signalMobileDeviceTaskCompletion(
                MobileDeviceContract.MobileDeviceDTTaskEndpoint.newBuilder().setMobileDeviceDTId(mobileDevices.get(0)).setTaskEndpointURI("example.com/doSomething").build());

        await().atMost(5, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS).untilAsserted(() -> {
            List<ResourcesManagementContract.ContainerInstanceDTO> containerInstanceList =
                    resourcesManagementClient.getCrudBlockingStub().retrieveContainerInstances(Empty.newBuilder().build()).getContainerInstancesList();
            assertThat(containerInstanceList).isEmpty();
        });

        List<MobileDeviceContract.TaskDTO> taskList =
                mobileDeviceClient.getCrudBlockingStub().retrieveTasks(Empty.newBuilder().build()).getTasksList();
        assertThat(taskList).isEmpty();

        List<ServicesManagementContract.WorkflowInstanceDTO> workflowInstanceList =
                servicesManagementClient.getCrudBlockingStub().retrieveWorkflowInstances(Empty.newBuilder().build()).getWorkflowInstancesList();
        assertThat(workflowInstanceList).isEmpty();

        List<ServicesManagementContract.ServiceInstanceDTO> serviceInstanceList =
                servicesManagementClient.getCrudBlockingStub().retrieveServiceInstances(Empty.newBuilder().build()).getServiceInstancesList();
        assertThat(serviceInstanceList).isEmpty();

        List<ResourcesManagementContract.NodeDTO> nodeList =
                resourcesManagementClient.getCrudBlockingStub().retrieveNodes(Empty.newBuilder().build()).getNodesList();
        for (ResourcesManagementContract.NodeDTO node : nodeList)
            assertThat(node.getOwnedContainerInstancesIdCount()).isEqualTo(0);
    }

    @AfterEach
    public void cleanInstances() {
        for (ResourcesManagementContract.ContainerInstanceDTO containerInstanceDTO :
                resourcesManagementClient.getCrudBlockingStub().retrieveContainerInstances(Empty.newBuilder().build()).getContainerInstancesList())
            resourcesManagementClient.getOperationalBlockingStub().destroyContainer(Int64Value.of(containerInstanceDTO.getId()));

        for (ServicesManagementContract.WorkflowInstanceDTO workflowInstanceDTO :
                servicesManagementClient.getCrudBlockingStub().retrieveWorkflowInstances(Empty.newBuilder().build()).getWorkflowInstancesList())
            servicesManagementClient.getOperationalBlockingStub().terminateWorkflowInstance(Int64Value.of(workflowInstanceDTO.getId()));

        for (MobileDeviceContract.MobileDeviceDTDTO mobileDeviceDTDTO :
                mobileDeviceClient.getCrudBlockingStub().retrieveMobileDeviceDTs(Empty.newBuilder().build()).getMobileDeviceDTsList()) {
            for (Long taskId : mobileDeviceDTDTO.getRunningTasksIdList()) {
                MobileDeviceContract.TaskDTO taskDTO = mobileDeviceClient.getCrudBlockingStub().getTask(Int64Value.of(taskId));
                mobileDeviceClient.getOperationalBlockingStub().signalMobileDeviceTaskCompletion(MobileDeviceContract.MobileDeviceDTTaskEndpoint.newBuilder()
                        .setMobileDeviceDTId(mobileDeviceDTDTO.getId())
                        .setTaskEndpointURI(taskDTO.getEndpointURI())
                        .build());
            }
        }
    }

    @AfterAll
    public static void cleanDbAndCloseChannels() {
        for (Long id : mobileDevices)
            mobileDeviceClient.getCrudBlockingStub().removeMobileDeviceDT(Int64Value.of(id));

        for (Long id : workflowTypes)
            servicesManagementClient.getCrudBlockingStub().removeWorkflowType(
                    ServicesManagementContract.RemoveWorkflowTypeParameters.newBuilder()
                            .setWorkflowTypeId(id)
                            .setRemoveEndpointServiceType(false)
                            .build());

        for (Long id : endPointServiceTypes)
            servicesManagementClient.getCrudBlockingStub().removeEndpointServiceType(Int64Value.of(id));

        for (Long id : serviceTypes)
            servicesManagementClient.getCrudBlockingStub().removeServiceType(Int64Value.of(id));

        for (Long id : nodes)
            resourcesManagementClient.getCrudBlockingStub().removeNode(Int64Value.of(id));

        for (Long id : containerTypes)
            resourcesManagementClient.getCrudBlockingStub().removeContainerType(Int64Value.of(id));

        mobileDeviceClient.closeChannel();
        servicesManagementClient.closeChannel();
        resourcesManagementClient.closeChannel();
    }
}
