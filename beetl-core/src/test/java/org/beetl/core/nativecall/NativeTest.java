package org.beetl.core.nativecall;

import java.util.List;

import org.beetl.core.BasicTestCase;
import org.beetl.core.Template;
import org.beetl.core.User;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

public class NativeTest extends BasicTestCase
{

	public static int DEFAULT_AGE = 11;
	public int age = 5;
	public static User[] users = new User[]
	{ User.getTestUser(), User.getTestUser() };

	@Test
	public void testAttr() throws Exception
	{
		NativeTest test = new NativeTest();
		Template t = gt.getTemplate("/nat/nat_attr_template.html");
		this.bind(t, "test", test);
		String str = t.render();
		AssertJUnit.assertEquals(this.getFileContent("/nat/nat_attr_expected.html"), str);

		t = gt.getTemplate("/nat/nat_attr_template.html");
		this.bind(t, "test", test);
		str = t.render();
		AssertJUnit.assertEquals(this.getFileContent("/nat/nat_attr_expected.html"), str);
	}

	@Test
	public void testMethod() throws Exception
	{
		NativeTest test = new NativeTest();
		Template t = gt.getTemplate("/nat/nat_method_template.html");
		this.bind(t, "test", test);
		String str = t.render();
		AssertJUnit.assertEquals(this.getFileContent("/nat/nat_method_expected.html"), str);

		t = gt.getTemplate("/nat/nat_method_template.html");
		this.bind(t, "test", test);
		str = t.render();
		AssertJUnit.assertEquals(this.getFileContent("/nat/nat_method_expected.html"), str);
	}

	@Test
	public void testArray() throws Exception
	{
		NativeTest test = new NativeTest();
		Template t = gt.getTemplate("/nat/nat_array_template.html");
		this.bind(t, "test", test);
		String str = t.render();
		AssertJUnit.assertEquals(this.getFileContent("/nat/nat_array_expected.html"), str);

		t = gt.getTemplate("/nat/nat_array_template.html");
		this.bind(t, "test", test);
		str = t.render();
		AssertJUnit.assertEquals(this.getFileContent("/nat/nat_array_expected.html"), str);
	}

	@Test
	public void testMethodParameter() throws Exception
	{
		NativeTest test = new NativeTest();
		Template t = gt.getTemplate("/nat/nat_method2_template.html");
		this.bind(t, "test", test);
		String str = t.render();
		AssertJUnit.assertEquals(this.getFileContent("/nat/nat_method2_expected.html"), str);

		t = gt.getTemplate("/nat/nat_method2_template.html");
		this.bind(t, "test", test);
		str = t.render();
		AssertJUnit.assertEquals(this.getFileContent("/nat/nat_method2_expected.html"), str);
	}

	@Test
	public void testAll() throws Exception
	{
		NativeTest test = new NativeTest();
		Template t = gt.getTemplate("/nat/all_template.html");
		this.bind(t, "test", test);
		String str = t.render();
		AssertJUnit.assertEquals(this.getFileContent("/nat/all_expected.html"), str);

		t = gt.getTemplate("/nat/all_template.html");
		this.bind(t, "test", test);
		str = t.render();
		AssertJUnit.assertEquals(this.getFileContent("/nat/all_expected.html"), str);
	}

	public String getText()
	{
		return "text";
	}

	public static String getDefaultText()
	{
		return "defaultText";
	}

	public User[] getMyFriends()
	{
		return users;
	}

	public static List<User> getUsers()
	{
		return User.getTestUsers();
	}

	public String getData(int i, String c)
	{
		return i + ":" + c;
	}

	public String getData(int i, String c, double d)
	{
		return i + ":" + c + ":" + d;
	}
}
