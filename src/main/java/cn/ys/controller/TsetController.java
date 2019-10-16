package cn.ys.controller;

import cn.ys.exception.SysException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("test")
public class TsetController {

    @RequestMapping("test")
    public String test222(){
        return "sss";
    }

    @RequestMapping("interceptor")
    public String say(){
        System.out.println("say()");
        return "success";
    }

    @RequestMapping("error")
    public String errtest() throws Exception{
        try {
            int a = 1/0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SysException("查询失败");
        }
        return "success";
    }

    @RequestMapping("fileupload3")
    public String upload2(@RequestParam("upload") MultipartFile up) throws IOException {
        String path = "http://localhost:8082/fileuploadserver_war_exploded/uploads/";
        Client client = Client.create();

        //创建客户端对象
        String name = up.getOriginalFilename();
        //和图片服务器进行连接
        WebResource webresource = client.resource(path + name);
        //上传文件
        webresource.put(up.getBytes());

        return "su";

    }


    @RequestMapping("fileupload2")
    public String upload2(HttpServletRequest request,@RequestParam("upload") MultipartFile up) throws IOException {
        String path = request.getSession().getServletContext().getRealPath("/upload");
        System.out.println(path);
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        String name = up.getOriginalFilename();
        up.transferTo(new File(path,name));
        return "su";
    }

    @RequestMapping("fileupload")
    public String upload(HttpServletRequest request) throws Exception {
        String path = request.getSession().getServletContext().getRealPath("/upload");
        System.out.println(path);
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);



        List<FileItem>  items = upload.parseRequest(request);
        System.out.println(items);
        for (FileItem item:items){
            if(item.isFormField()){
                System.out.println("if()");
            }else{
                String uuid = UUID.randomUUID().toString().replace("-", "");

                String name = uuid+item.getName();
                System.out.println(name);
                item.write(new File(path,name));
                item.delete();
            }
        }
        return "su";
    }
}
