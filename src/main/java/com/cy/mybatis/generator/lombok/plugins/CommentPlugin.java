package com.cy.mybatis.generator.lombok.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author cy
 * @date 2019-06-03
 */
@SuppressWarnings("unused")
public class CommentPlugin extends PluginAdapter {
    private static final String PARAMETER_TYPE = "parameterType";

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        topLevelClass.getJavaDocLines().clear();
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * Table: " + introspectedTable.getFullyQualifiedTable());
        topLevelClass.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field,
                                       TopLevelClass topLevelClass,
                                       IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable,
                                       ModelClassType modelClassType) {
        field.getJavaDocLines().clear();
        field.addJavaDocLine("/**");
        String remark = introspectedColumn.getRemarks();
        if (remark != null && remark.length() > 1) {
            field.addJavaDocLine(" * " + remark);
            field.addJavaDocLine(" *");
        }
        field.addJavaDocLine(" * Column:    " + introspectedColumn.getActualColumnName());
        field.addJavaDocLine(" * Nullable:  " + introspectedColumn.isNullable());
        field.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass,
                                              IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        return true;
    }

    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element,
                                                               IntrospectedTable introspectedTable) {
        this.commentResultMap(element, introspectedTable);
        return true;
    }

    @Override
    public boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element,
                                                            IntrospectedTable introspectedTable) {
        this.commentResultMap(element, introspectedTable);
        return true;
    }

    private void commentResultMap(XmlElement element, IntrospectedTable introspectedTable) {
        List<Element> es = element.getElements();
        if (!es.isEmpty()) {
            String alias = introspectedTable.getTableConfiguration().getAlias();
            int aliasLen = -1;
            if (alias != null) {
                aliasLen = alias.length() + 1;
            }

            Iterator<Element> it = es.iterator();
            HashMap map = new HashMap();

            while (true) {
                while (it.hasNext()) {
                    Element e = it.next();
                    if (e instanceof TextElement) {
                        it.remove();
                    } else {
                        XmlElement el = (XmlElement) e;
                        List<Attribute> as = el.getAttributes();
                        if (!as.isEmpty()) {
                            String col = null;
                            Iterator iterator = as.iterator();

                            while (iterator.hasNext()) {
                                Attribute a = (Attribute) iterator.next();
                                if ("column".equalsIgnoreCase(a.getName())) {
                                    col = a.getValue();
                                    break;
                                }
                            }

                            if (col != null) {
                                if (aliasLen > 0) {
                                    col = col.substring(aliasLen);
                                }

                                IntrospectedColumn ic = introspectedTable.getColumn(col);
                                if (ic != null) {
                                    StringBuilder sb = new StringBuilder();
                                    if (ic.getRemarks() != null && ic.getRemarks().length() > 1) {
                                        sb.append("<!-- ");
                                        sb.append(ic.getRemarks());
                                        sb.append(" -->");
                                        map.put(el, new TextElement(sb.toString()));
                                    }
                                }
                            }
                        }
                    }
                }

                if (map.isEmpty()) {
                    return;
                }

                Set<Element> set = map.keySet();
                Iterator iterator = set.iterator();

                while (iterator.hasNext()) {
                    Element e = (Element) iterator.next();
                    int id = es.indexOf(e);
                    es.add(id, (Element) map.get(e));
                    es.add(id, new TextElement(""));
                }

                return;
            }
        }
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element,
                                                IntrospectedTable introspectedTable) {
        this.removeAttribute(element.getAttributes(), PARAMETER_TYPE);
        return true;
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element,
                                                         IntrospectedTable introspectedTable) {
        this.removeAttribute(element.getAttributes(), PARAMETER_TYPE);
        return true;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element,
                                                                     IntrospectedTable introspectedTable) {
        this.removeAttribute(element.getAttributes(), PARAMETER_TYPE);
        return true;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element,
                                                                     IntrospectedTable introspectedTable) {
        this.removeAttribute(element.getAttributes(), PARAMETER_TYPE);
        return true;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element,
                                                                        IntrospectedTable introspectedTable) {
        this.removeAttribute(element.getAttributes(), PARAMETER_TYPE);
        return true;
    }

    private void removeAttribute(List<Attribute> as, String name) {
        if (!as.isEmpty()) {
            Iterator it = as.iterator();

            Attribute attr;
            do {
                if (!it.hasNext()) {
                    return;
                }

                attr = (Attribute) it.next();
            } while (!attr.getName().equalsIgnoreCase(name));

            it.remove();
        }
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        document.getRootElement().addElement(new TextElement(""));
        document.getRootElement().addElement(new TextElement("<!-- Generate time: " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + " ### -->\n\n"));
        document.getRootElement().addElement(new TextElement("<!-- Your codes goes here!!! -->"));
        document.getRootElement().addElement(new TextElement(""));
        return true;
    }
}
