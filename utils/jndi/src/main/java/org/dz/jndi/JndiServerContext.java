package org.dz.jndi;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.Binding;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import javax.naming.OperationNotSupportedException;

public class JndiServerContext implements JndiContext {
	
	public static Integer SERVER_PORT = null;
	
	private Map<String,Object> context;
	private Hashtable<String, Object> environment;
	
	public JndiServerContext() {
		this(new Hashtable<String, Object>());
	}

	public JndiServerContext(Hashtable<String, Object> environment) {
		this.context = new HashMap<String, Object>();
		this.environment = environment;
		if ( environment != null ) {
			try {
				String providerUrl = (String) environment.get(Context.PROVIDER_URL);
				URI uri = new URI(providerUrl);
				SERVER_PORT = uri.getPort();
			} catch (URISyntaxException e) {
				throw new JndiException(e);
			}
		}
	}

	@Override
	public synchronized Object addToEnvironment(String propName, Object propVal) throws NamingException {
		return environment.put(propName, propVal);
	}

	@Override
	public synchronized void bind(Name name, Object obj) throws NamingException {
		bind(name.toString(), obj);
	}

	@Override
	public synchronized void bind(String name, Object obj) throws NamingException {
		context.put(name, obj);
	}

	@Override
	public void close() throws NamingException {
		// Rien à faire
	}

	@Override
	public Name composeName(Name name, Name prefix) throws NamingException {
		Name result = (Name) prefix.clone();
        result.addAll(name);
        return result;
	}

	@Override
	public String composeName(String name, String prefix) throws NamingException {
		CompositeName result = new CompositeName(prefix);
        result.addAll(new CompositeName(name));
        return result.toString();
	}

	@Override
	public Context createSubcontext(Name name) throws NamingException {
		return createSubcontext(name.toString());
	}

	@Override
	public Context createSubcontext(String name) throws NamingException {
		throw new OperationNotSupportedException();
	}

	@Override
	public void destroySubcontext(Name name) throws NamingException {
		destroySubcontext(name.toString());
	}

	@Override
	public void destroySubcontext(String name) throws NamingException {
		throw new OperationNotSupportedException();
	}

	@Override
	public Hashtable<String, Object> getEnvironment() throws NamingException {
		return environment;
	}

	@Override
	public String getNameInNamespace() throws NamingException {
		throw new OperationNotSupportedException();
	}

	@Override
	public NameParser getNameParser(Name name) throws NamingException {
		return getNameParser(name.toString());
	}

	@Override
	public NameParser getNameParser(String name) throws NamingException {
		throw new OperationNotSupportedException();
	}

	@Override
	public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
		return list(name.toString());
	}

	@Override
	public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
		Object o = lookup(name);
        if (o == this) {
            return new ListEnumeration();
        }
        else if (o instanceof Context) {
            return ((Context) o).list("");
        }
        else {
            throw new NotContextException();
        }
	}

	@Override
	public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
		return listBindings(name.toString());
	}

	@Override
	public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
		Object o = lookup(name);
        if (o == this) {
            return new ListBindingEnumeration();
        }
        else if (o instanceof Context) {
            return ((Context) o).listBindings("");
        }
        else {
            throw new NotContextException();
        }
	}

	@Override
	public Object lookup(Name name) throws NamingException {
		return lookup(name.toString());
	}

	@Override
	public Object lookup(String name) throws NamingException {
		return context.get(name);
	}

	@Override
	public Object lookupLink(Name name) throws NamingException {
		return lookupLink(name.toString());
	}

	@Override
	public Object lookupLink(String name) throws NamingException {
		return lookup(name);
	}

	@Override
	public synchronized void rebind(Name name, Object obj) throws NamingException {
		rebind(name.toString(), obj);
	}

	@Override
	public synchronized void rebind(String name, Object obj) throws NamingException {
		unbind(name);
		bind(name, obj);
	}

	@Override
	public synchronized Object removeFromEnvironment(String propName) throws NamingException {
		return environment.remove(propName);
	}

	@Override
	public synchronized void rename(Name oldName, Name newName) throws NamingException {
		rename(oldName.toString(), newName.toString());
	}

	@Override
	public synchronized void rename(String oldName, String newName) throws NamingException {
		Object obj = lookup(oldName);
		if ( obj != null ) {
			unbind(oldName);
			bind(newName, obj);
		}
	}

	@Override
	public synchronized void unbind(Name name) throws NamingException {
		unbind(name.toString());
	}

	@Override
	public synchronized void unbind(String name) throws NamingException {
		context.remove(name);
	}

	@Override
	public JndiReturn executeCall(JndiCall call) throws Exception {
		return new JndiReturn(call.getMethod().invoke(this, call.getArgs()), null);
	}
	
	private abstract class LocalNamingEnumeration<T> implements NamingEnumeration<T> {
        private Iterator<Entry<String,Object>> i = JndiServerContext.this.context.entrySet().iterator();

        public boolean hasMore() throws NamingException {
            return i.hasNext();
        }

        public boolean hasMoreElements() {
            return i.hasNext();
        }

        protected Entry<String,Object> getNext() {
            return i.next();
        }

        public void close() throws NamingException {
        }
    }
	
	private class ListEnumeration extends LocalNamingEnumeration<NameClassPair> {
        public NameClassPair next() throws NamingException {
            return nextElement();
        }

        public NameClassPair nextElement() {
            Entry<String,Object> entry = getNext();
            return new NameClassPair(entry.getKey(), entry.getValue().getClass().getName());
        }
    }

	private class ListBindingEnumeration extends LocalNamingEnumeration<Binding> {
        public Binding next() throws NamingException {
            return nextElement();
        }

        public Binding nextElement() {
            Entry<String,Object> entry = getNext();
            return new Binding(entry.getKey(), entry.getValue());
        }
    }
}
