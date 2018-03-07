package fmyxyz.mybatis_generatorConfig;

import java.io.Serializable;
/**
 * 
 * @author fmy-leaves
 *
 */
public class SysObjects implements Serializable {
	private static final long serialVersionUID = -1347655914834092120L;
	private String name;
	private String xtype;
	private String type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getXtype() {
		return xtype;
	}

	public void setXtype(String xtype) {
		this.xtype = xtype;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
