package server.utility;


import common.interaction.Request;
import common.interaction.Response;
import common.interaction.ResponseCode;
import common.utility.Outputer;
import server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.*;



/**
 * Handles user connection.
 */

public class ConnectionHandler implements Runnable {

    private Server server;

    private Socket clientSocket;

    private CommandManager commandManager;

    private ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    /**
     * ForkJoinPool là một lớp trong Java, nằm trong gói java.util.concurrent,
     * cung cấp một thread pool cho việc xử lý song song trên cấu trúc chia để trị (divide-and-conquer).
     * Nó được thiết kế đặc biệt để xử lý các công việc đệ quy hoặc phân tách thành các phần nhỏ hơn và
     * thực hiện chúng một cách song song.
     *
     * ForkJoinPool là một dạng đặc biệt của ExecutorService, có sự tối ưu hóa cho việc xử lý các công
     * việc đệ quy có tính chất chia để trị. Nó sử dụng mô hình "fork-join" trong đó các công việc lớn
     * được chia nhỏ thành các công việc nhỏ hơn và được thực hiện bất đồng bộ trên các luồng khác nhau
     * trong pool. Khi các công việc con hoàn thành, kết quả của chúng được kết hợp lại.
     *
     * => Tóm lại cái này để chia nhỏ công việc để xử lý đồng thời.
     */

    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);

    public ConnectionHandler(Server server, Socket clientSocket, CommandManager commandManager) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.commandManager = commandManager;
    }

    /**
     * Main handling cycle.
     */

    @Override
    public void run() {
        Request userRequest = null;
        Response responseToUser = null;
        boolean stopFlag = false;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            do {
                userRequest = (Request) clientReader.readObject();
                responseToUser = forkJoinPool.invoke(new HandleRequestTask(userRequest, commandManager));
                Response finalResponseToUser = responseToUser;
                if (!fixedThreadPool.submit(() -> {
                    try {
                        clientWriter.writeObject(finalResponseToUser);
                        clientWriter.flush();
                        return true;
                    } catch (IOException exception) {
                        Outputer.printerror("Произошла ошибка при отправке данных на клиент!");
                    }
                    return false;
                }).get()) break;
            } while (responseToUser.getResponseCode() != ResponseCode.SERVER_EXIT &&
                    responseToUser.getResponseCode() != ResponseCode.CLIENT_EXIT);
            if (responseToUser.getResponseCode() == ResponseCode.SERVER_EXIT)
                stopFlag = true;
        } catch (ClassNotFoundException exception) {
            Outputer.printerror("Произошла ошибка при чтении полученных данных!");
        } catch (CancellationException | ExecutionException | InterruptedException exception) {
            Outputer.println("При обработке запроса произошла ошибка многопоточности!");
        } catch (IOException exception) {
            Outputer.printerror("Непредвиденный разрыв соединения с клиентом!");
        } finally {
            try {
                fixedThreadPool.shutdown();
                clientSocket.close();
                Outputer.println("Клиент отключен от сервера.");
            } catch (IOException exception) {
                Outputer.printerror("Произошла ошибка при попытке завершить соединение с клиентом!");
            }
            if (stopFlag) server.stop();
        }
    }


}
