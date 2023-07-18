package io.github.lorenzobucci.tesi.metamodel.mobiledevice.service.gRPC;

import com.google.protobuf.Empty;
import io.github.lorenzobucci.tesi.metamodel.mobiledevice.dao.TaskDao;
import io.github.lorenzobucci.tesi.metamodel.mobiledevice.model.Task;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Inject;

import java.util.List;

public class TaskService extends TaskServiceGrpc.TaskServiceImplBase {

    @Inject
    private TaskDao taskDao;

    @Override
    public void getTask(MobileDevice.TaskDTO request, StreamObserver<MobileDevice.TaskDTO> responseObserver) {
        Task task = taskDao.findById(request.getId());

        MobileDevice.TaskDTO taskDTO = MobileDevice.TaskDTO.newBuilder().setId(task.getId())
                .setEndpointURI(task.getEndpoint().toString())
                .setParameters(task.getParameters())
                .build();

        responseObserver.onNext(taskDTO);
        responseObserver.onCompleted();
    }

    @Override
    public void retrieveTasks(Empty request, StreamObserver<MobileDevice.TaskList> responseObserver) {
        List<Task> tasks = taskDao.findAll();
        MobileDevice.TaskList.Builder taskList = MobileDevice.TaskList.newBuilder();

        for (Task task : tasks) {
            MobileDevice.TaskDTO taskDTO = MobileDevice.TaskDTO.newBuilder().setId(task.getId())
                    .setEndpointURI(task.getEndpoint().toString())
                    .setParameters(task.getParameters())
                    .build();

            taskList.addTasks(taskDTO);
        }

        responseObserver.onNext(taskList.build());
        responseObserver.onCompleted();
    }
}
