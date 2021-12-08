package cn.flood.excel.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import cn.flood.excel.annotation.ResponseExcel;
import cn.flood.excel.config.ExcelConfigProperties;
import cn.flood.excel.kit.ExcelException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;


import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lengleng
 * @date 2020/3/29
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ManySheetWriteHandler extends AbstractSheetWriteHandler {
	private final ExcelConfigProperties configProperties;

	/**
	 * 当且仅当List不为空且List中的元素也是List 才返回true
	 *
	 * @param obj 返回对象
	 * @return
	 */
	@Override
	public boolean support(Object obj) {
		if (obj instanceof List) {
			List objList = (List) obj;
			return !objList.isEmpty()&&objList.get(0) instanceof List;
		} else {
			throw new ExcelException("@ResponseExcel 返回值必须为List类型");
		}
	}

	@Override
	@SneakyThrows
	public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
		List objList = (List) obj;
		List eleList = (List) objList.get(0);

		ExcelWriter excelWriter = getExcelWriter(response, responseExcel, eleList, configProperties.getTemplatePath());

		String[] sheets = responseExcel.sheet();
		for (int i = 0; i < sheets.length; i++) {
			//创建sheet
			WriteSheet sheet;
			if (StringUtils.hasText(responseExcel.template())) {
				sheet = EasyExcel.writerSheet(i).build();
			} else {
				sheet = EasyExcel.writerSheet(i, sheets[i]).build();
			}

			// 写入sheet
			excelWriter.write((List) objList.get(i), sheet);
		}
		excelWriter.finish();
	}
}
