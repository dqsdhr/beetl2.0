package org.beetl.core.engine;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Listener;
import org.beetl.core.exception.BeetlException;
import org.beetl.core.statement.ASTNode;
import org.beetl.core.statement.BlockStatement;
import org.beetl.core.statement.Statement;

/**
 * @author joelli 遍历语句，如果找到匹配的，执行相应的listeners方法
 * 
 */
public class StatementParser
{

	Map<Class, Listener> listeners = new HashMap<Class, Listener>();
	BlockStatement block = null;
	Map firstNode = new HashMap(2);

	public StatementParser(Statement[] sts, GroupTemplate gt, String resourceId)
	{
		block = new BlockStatement(sts, null);
		firstNode.put("groupTemplate", gt);
		firstNode.put("resourceId", resourceId);
	}

	public void addListener(Class c, Listener ls)
	{
		listeners.put(c, ls);
	}

	public void parse()
	{
		Stack<Object> stack = new Stack<Object>();
		stack.push(firstNode);
		Class[] matchClasses = listeners.keySet().toArray(new Class[0]);

		this.exec(block, matchClasses, stack);
	}

	protected void exec(Object astNode, Class[] matchClasses, Stack stack)
	{

		stack.push(astNode);
		Class astNodeClass = astNode.getClass();
		Field[] fields = astNodeClass.getFields();
		for (Field f : fields)
		{
			if (f.getModifiers() == Modifier.PUBLIC)
			{
				Class target = null;
				Class target2 = null;
				// Class c = f.getDeclaringClass();
				Class c = f.getType();
				if (c.isArray())
				{
					target = c.getComponentType();
				}
				else
				{
					target = c;
				}

				boolean isArray = false;

				// 只解析含有ASTNode的字段
				if (ASTNode.class.isAssignableFrom(target))
				{
					Object values;
					try
					{
						values = f.get(astNode);

						if (values == null)
							continue;
						else
						{
							target2 = values.getClass();
							if (target2.isArray())
							{
								if (((Object[]) values).length == 0)
								{
									continue;
								}
								target2 = target2.getComponentType();

								isArray = true;
							}
						}
					}
					catch (Exception e)
					{
						throw new RuntimeException(e);
					}

					for (Class expected : matchClasses)
					{
						// 如果匹配上
						if (isArray)
						{
							Object[] targetValue = (Object[]) values;

							for (int i = 0; i < targetValue.length; i++)
							{
								Object o = targetValue[i];
								if (o == null)
									continue;
								if (expected.isAssignableFrom(o.getClass()))
								{
									stack.push(o);
									Listener ls = this.listeners.get(expected);
									NodeEvent e = new NodeEvent(stack);
									Object newASTNode = ls.onEvent(e);
									if (newASTNode != null)
									{
										stack.pop();
										stack.push(newASTNode);
										//替换原有节点
										targetValue[i] = newASTNode;
										o = newASTNode;
									}
									// 继续遍历子节点
									this.exec(o, matchClasses, stack);
									stack.pop();
									continue;
								}

							}
						}
						else
						{
							if (expected.isAssignableFrom(values.getClass()))
							{
								stack.push(values);

								Listener ls = this.listeners.get(expected);
								NodeEvent e = new NodeEvent(stack);
								Object newASTNode = ls.onEvent(e);

								if (newASTNode != null)
								{
									stack.pop();
									stack.push(newASTNode);
									//替换原有节点
									try
									{
										f.set(astNode, values);
									}
									catch (Exception ex)
									{
										BeetlException be = new BeetlException(BeetlException.ERROR, "替换ASTNode错", ex);
										be.token = ((ASTNode) newASTNode).token;
										throw be;
									}

									values = newASTNode;
								}
								this.exec(values, matchClasses, stack);

								stack.pop();
								continue;
							}
						}

						// 没有匹配上，继续遍历
						if (isArray)
						{
							ASTNode[] astNodes = (ASTNode[]) values;
							for (ASTNode node : astNodes)
							{
								this.exec(node, matchClasses, stack);
							}
						}
						else
						{
							ASTNode node = (ASTNode) values;
							this.exec(node, matchClasses, stack);
						}

					}
				}

			}

		}
		stack.pop();

	}

}
