/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jpb.util.test;

import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Test;

import br.com.jpb.util.StringUtil;

/**
 * 
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class StringUtilTest {

	@Test
	public void testLimitToBytesAtLastSpace() {
		String s1 = "Associação Brasileira de Otorrinolaringologia e Cirurgia Cérvico Facial";
		String s1Limited = StringUtil.limitToBytesAtLastSpace(s1, 60);
		System.out.println(s1Limited);
		Assert.assertEquals(s1Limited,
				"Associação Brasileira de Otorrinolaringologia e Cirurgia");
		String s2 = "AssociaçãoBrasileiradeOtorrinolaringologiaeCirurgiaCérvicoFacial";
		String s2Limited = StringUtil.limitToBytesAtLastSpace(s2, 60);
		System.out.println(s2Limited);
		Assert.assertEquals(s2Limited,
				"AssociaçãoBrasileiradeOtorrinolaringologiaeCirurgiaCérvicoFa");
		String s3 = new String(
				"Associação Brasileira de Otorrinolaringologia e Cirurgia Cérvico Facial"
						.getBytes(),
				Charset.forName("UTF-8"));
		String s3Limited = StringUtil.limitToBytesAtLastSpace(s3, 60);
		System.out.println(s3Limited);
		Assert.assertEquals(
				s3Limited,
				new String(
						"Associação Brasileira de Otorrinolaringologia e Cirurgia"
								.getBytes(), Charset.forName("UTF-8")));
		String s4 = new String(
				"AssociaçãoBrasileiradeOtorrinolaringologiaeCirurgiaCérvicoFacial"
						.getBytes(),
				Charset.forName("UTF-8"));
		String s4Limited = StringUtil.limitToBytesAtLastSpace(s4, 60);
		System.out.println(s4Limited);
		Assert.assertEquals(
				s4Limited,
				new String(
						"AssociaçãoBrasileiradeOtorrinolaringologiaeCirurgiaCérvicoFa"
								.getBytes(), Charset.forName("UTF-8")));
	}

	@Test
	public void testSubstringInTheLastSpaceBefore() {
		Assert.assertEquals(null,
				StringUtil.substringInTheLastSpaceBefore(null, 3000));
		Assert.assertEquals(
				"Associação Brasileira de Otorrinolaringologia e Cirurgia",
				StringUtil
						.substringInTheLastSpaceBefore(
								"Associação Brasileira de Otorrinolaringologia e Cirurgia",
								3000));

		String s = "Mussum ipsum cacilds, vidis litro abertis. Consetis adipiscings elitis. Pra lá , depois divoltis porris, paradis. Paisis, filhis, espiritis santis. "
				+ "Mé faiz elementum girarzis, nisi eros vermeio, in elementis mé pra quem é amistosis quis leo. Manduma pindureta quium dia nois paga. "
				+ "Sapien in monti palavris qui num significa nadis i pareci latim. Interessantiss quisso pudia ce receita de bolis, mais bolis eu num gostis. "
				+ "Suco de cevadiss, é um leite divinis, qui tem lupuliz, matis, aguis e fermentis. Interagi no mé, cursus quis, vehicula ac nisi. Aenean vel dui dui. "
				+ "Nullam leo erat, aliquet quis tempus a, posuere ut mi. Ut scelerisque neque et turpis posuere pulvinar pellentesque nibh ullamcorper. "
				+ "Pharetra in mattis molestie, volutpat elementum justo. Aenean ut ante turpis. Pellentesque laoreet mé vel lectus scelerisque interdum cursus velit auctor. "
				+ "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ac mauris lectus, non scelerisque augue. Aenean justo massa. "
				+ "Casamentiss faiz malandris se pirulitá, Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. "
				+ "Lorem ipsum dolor sit amet, consectetuer Ispecialista im mé intende tudis nuam golada, vinho, uiski, carirí, rum da jamaikis, só num pode ser mijis. "
				+ "Adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. "
				+ "Cevadis im ampola pa arma uma pindureta. Nam varius eleifend orci, sed viverra nisl condimentum ut. Donec eget justis enim. Atirei o pau no gatis. "
				+ "Viva Forevis aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. "
				+ "Copo furadis é disculpa de babadis, arcu quam euismod magna, bibendum egestas augue arcu ut est. Delegadis gente finis. In sit amet mattis porris, paradis. Paisis, filhis, espiritis santis. "
				+ "Mé faiz elementum girarzis. Pellentesque viverra accumsan ipsum elementum gravidis.";

		String expected = "Mussum ipsum cacilds, vidis litro abertis. Consetis adipiscings elitis. Pra lá , depois divoltis porris, paradis. Paisis, filhis, espiritis santis. "
				+ "Mé faiz elementum girarzis, nisi eros vermeio, in elementis mé pra quem é amistosis quis leo. Manduma pindureta quium dia nois paga. "
				+ "Sapien in monti palavris qui num significa nadis i pareci latim. Interessantiss quisso pudia ce receita de bolis, mais bolis eu num gostis. "
				+ "Suco de cevadiss, é um leite divinis, qui tem lupuliz, matis, aguis e fermentis. Interagi no mé, cursus quis, vehicula ac nisi. Aenean vel dui dui. "
				+ "Nullam leo erat, aliquet quis tempus a, posuere ut mi. Ut scelerisque neque et turpis posuere pulvinar pellentesque nibh ullamcorper. "
				+ "Pharetra in mattis molestie, volutpat elementum justo. Aenean ut ante turpis. Pellentesque laoreet mé vel lectus scelerisque interdum cursus velit auctor. "
				+ "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ac mauris lectus, non scelerisque augue. Aenean justo massa. "
				+ "Casamentiss faiz";

		Assert.assertEquals(expected,
				StringUtil.substringInTheLastSpaceBefore(s, 1000));
	}
}