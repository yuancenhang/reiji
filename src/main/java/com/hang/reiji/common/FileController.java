package com.hang.reiji.common;

import com.hang.reiji.domain.R;
import com.hang.reiji.utils.UtilOne;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/common")
public class FileController {

    @Value("${filePath.picture}")
    private String path;

    /**
     * 文件上传
     * 文件上传对前端的要求：
     *      必须用form表单，method=post，enctype=multipart格式，input的type=file
     * 后端要接收的要求：
     *      使用commons-fileupload,common-io
     *      spring-web包里对这两个东西封装后，变成了MultipartFile类，可以直接拿来用
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        /*file就是接收到的文件.这里的file变量名，必须和前端的name相同，不然没法接收
            服务器收到文件后，会暂时把它放在本地的某个目录。如果不保存，在请求结束后就会删除这个文件。
            所以，要把文件保存到本地。
         */
        //这是文件上传时候的名字，xxx.jsp
        String name = file.getOriginalFilename();

        //截取文件后缀，先获取最后一个.的下标，然后截取
        int i = name.lastIndexOf(".");
        //截取后缀，i就是起点下标
        String suffix = name.substring(i);
        //拼成新文件名。为了避免文件同名
        String newName = UtilOne.getUUID().toString() + suffix;
        //创建文件夹，如果不存在就新建一个
        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdir();
        }
        //把上传的文件转存到指定目录。这里的转存有现成的方法，不需要自己用流来写
        try {
            file.transferTo(new File(path + newName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(newName);
    }

    /**
     * 文件下载
     *      下载后前端有两种选择，一是直接显示在页面，二是保存到自己的电脑。
     *      这里不需要返回什么数据，所以是void。
     *      但是这里需要用响应流来把文件写给浏览器，所以用了response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            FileInputStream stream = new FileInputStream(new File(path + name));
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            byte[] bytes = new byte[2048];
            int len = 0;
            while ((len = stream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            stream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
