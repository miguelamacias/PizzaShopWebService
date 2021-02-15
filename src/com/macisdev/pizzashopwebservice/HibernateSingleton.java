package com.macisdev.pizzashopwebservice;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSingleton {
	private static final SessionFactory ourSessionFactory;
	private static final Session session;

	static {
		try {
			Configuration configuration = new Configuration();
			configuration.configure("com/macisdev/pizzashopwebservice/hibernate.cfg.xml");

			ourSessionFactory = configuration.buildSessionFactory();
			session = ourSessionFactory.openSession();
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static Session getSession() throws HibernateException {
		return session;
	}
}
