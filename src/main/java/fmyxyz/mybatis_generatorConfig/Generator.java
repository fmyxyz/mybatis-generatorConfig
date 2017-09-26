package fmyxyz.mybatis_generatorConfig;

import java.io.FileWriter;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * Hello world!
 *
 */
@Service
public class Generator {
	@Value("${tablePrefix}")
	private String tablePrefix = "t";
	private String[] tablePrefixs;
	@Value("${tableSuffix}")
	private String tableSuffix = "";
	@Value("${division}")
	private String division = "_";
	@Value("${dtoPrefix}")
	private String dtoPrefix = "";
	@Value("${dtoSuffix}")
	private String dtoSuffix = "";
	@Value("${targetPackage}")
	private String targetPackage = "";
	@Value("${targetProject}")
	private String targetProject = "";

	@Autowired
	private SysMapper sysMapper;

	@Autowired
	private DruidDataSource dataSource;

	public void generate() throws Exception {
		// 处理多个表前缀
		tablePrefixs = tablePrefix.split(",");
		// 解析模板文档
		Document doc = new SAXReader().read(getClass().getResourceAsStream("/mybatis/generatorConfig.xml"));
		doc.setXMLEncoding("UTF-8");
		Element rootElem = doc.getRootElement();
		Element context = rootElem.element("context");
		// 配置数据源
		configDataSource(context);
		// 配置DTO的输出
		configDTO(context);
		// 配置Mapper的输出
		configMapper(context);
		// 配置dao的输出
		configDAO(context);
		// 获取表对象
		List<SysObjects> os = sysMapper.getTables(dataSource.getUrl().split(":")[1]);
		// 生成表定义
		createTableElem(context, os);
		// 输出自动生成配置文件
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter write = new XMLWriter(new FileWriter("generatorConfig.xml"), format);
		write.write(doc);
		write.close();
	}

	private void createTableElem(Element context, List<SysObjects> os) {
		for (SysObjects sysObjects : os) {
			String tname = sysObjects.getName();
			tname = tname.toLowerCase();
			for (String s : tablePrefixs) {
				if (tname.startsWith(s)) {
					tname = tname.replaceFirst(s, "");
					break;
				}
			}

			String[] ci = tname.split(division);
			String newName = "";

			for (int i = 0; i < ci.length; i++) {
				String str = ci[i];
				// 97-122 --- a-z
				char[] cs = str.toCharArray();
				if (cs.length > 0) {
					if (97 <= cs[0] && cs[0] <= 122) {
						cs[0] -= 32;
					}
				}
				newName += String.valueOf(cs);
			}
			newName += dtoSuffix;

			Element table = DocumentHelper.createElement("table");
			table.addAttribute("tableName", sysObjects.getName());
			table.addAttribute("domainObjectName", newName);
			table.addAttribute("enableCountByExample", "false");
			table.addAttribute("enableUpdateByExample", "false");
			table.addAttribute("enableDeleteByExample", "false");
			table.addAttribute("enableSelectByExample", "false");
			table.addAttribute("selectByExampleQueryId", "false");
			context.add(table);
		}
	}

	private void configDAO(Element context) {
		Element javaClientGenerator = context.element("javaClientGenerator");
		javaClientGenerator.addAttribute("targetPackage", targetPackage + "dao");
		javaClientGenerator.addAttribute("targetProject", targetProject);
	}

	private void configMapper(Element context) {
		Element sqlMapGenerator = context.element("sqlMapGenerator");
		sqlMapGenerator.addAttribute("targetPackage", targetPackage);
		sqlMapGenerator.addAttribute("targetProject", targetProject);
	}

	private void configDTO(Element context) {
		Element javaModelGenerator = context.element("javaModelGenerator");
		javaModelGenerator.addAttribute("targetPackage", targetPackage + ".dto");
		javaModelGenerator.addAttribute("targetProject", targetProject);
	}

	private void configDataSource(Element context) {
		Element jc = context.element("jdbcConnection");
		jc.addAttribute("driverClass", dataSource.getDriverClassName());
		jc.addAttribute("connectionURL", dataSource.getUrl());
		jc.addAttribute("userId", dataSource.getUsername());
		jc.addAttribute("password", dataSource.getPassword());
	}
}
