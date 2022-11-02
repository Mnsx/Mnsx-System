package top.mnsx.mnsx_system.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import org.springframework.stereotype.Service;
import top.mnsx.mnsx_system.service.ImportExportService;
import top.mnsx.mnsx_system.dto.ExportUserDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/11/2 15:37
 * @Description: Excel导出导入功能实现
 */
@Service
public class ExcelServiceImpl implements ImportExportService {

    public void writeExcel(HttpServletResponse response, String sheetName, Supplier<Collection<?>> supplier) {
        try {
            EasyExcel.write(response.getOutputStream(), ExportUserDTO.class).sheet(sheetName).doWrite(supplier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> void readExcel(InputStream inputStream, String sheetName, Class<?> T, Consumer<List<T>> consumer) {
        EasyExcel.read(inputStream, T, new PageReadListener<>(consumer)).sheet(sheetName).doRead();
    }
}
