package cn.varin.varaiagent.chatMemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileChatMemory implements ChatMemory {

    // 文件路径
    private    final String  BASE_DIR;
    private  static  final Kryo kryo  = new Kryo();;
    static {
        kryo.setRegistrationRequired(false);

        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

    }
    public FileChatMemory(String dir) {
        this.BASE_DIR = dir;
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }

    }

    @Override
    public void add(String conversationId, Message message) {
        List<Message> messages = getMessages(conversationId);
        messages.add(message);
        save(conversationId, messages);
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> messageList = getMessages(conversationId);
        messageList.addAll(messages);
        save(conversationId, messageList);


    }

    @Override
    public List<Message> get(String conversationId, int lastN) {

        List<Message> messages = getMessages(conversationId);

        return  messages.stream()
                .skip(Math.max(0,messages.size()-lastN))
                .toList();


    }

    @Override
    public void clear(String conversationId) {
        File file = getFile(conversationId);
        if (file.exists()) {
            file.delete();
        }
    }


    /**
     * 获取到文件
     * @param id
     * @return
     */
    private  File getFile(String id) {
        return new File(BASE_DIR , id+".kryo");
    }

    /**
     * 保存信息
     * @param id
     * @param messages
     */
    private void save(String id, List<Message> messages) {
        File file = getFile(id);
        try (Output output = new Output(new FileOutputStream(file))) { // 使用 try-with-resources 自动关闭
            kryo.writeObject(output, messages);
            // 无需手动关闭，try-with-resources 会自动处理
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * id查询信息
     */
    private List<Message> getMessages(String id) {
        File file = getFile(id);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (Input input = new Input(new FileInputStream(file))) { // 同样使用 try-with-resources
            // 使用泛型类型读取
            return kryo.readObject(input, ArrayList.class);
        } catch (IOException e) {
            // 若文件损坏，可删除文件并返回空列表
            file.delete();
            return new ArrayList<>();
        }
    }
}