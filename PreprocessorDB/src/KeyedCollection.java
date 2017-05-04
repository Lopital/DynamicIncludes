import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class KeyedCollection<K, V> extends AbstractCollection<V> {

	protected final Map<K, V> map;

	public KeyedCollection() {
		map = new LinkedHashMap<>();
	}

	protected abstract K getKey(V value);

	public V get(K key) {
		return map.get(key);
	}
	
	@Override
	public Iterator<V> iterator() {
		return map.values().iterator();
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean add(V value) {
		if (value == null) {
			throw new NullPointerException();
		}
		K key = getKey(value);
		if (map.containsKey(key)) {
			return false;
		} else {
			map.put(key, value);
			return true;
		}
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean contains(Object o) {
		V value = (V) o;
		return containsValue(value);
	}

	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	public boolean containsValue(V value) {
		if (value == null) {
			throw new NullPointerException();
		}
		K key = getKey(value);
		return map.containsKey(key) && map.get(key).equals(value);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return super.containsAll(c);
	}

	public boolean containsAllKeys(Collection<K> c) {
		return map.keySet().containsAll(c);
	}

	public boolean containsAllValues(Collection<V> c) {
		return map.values().containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean remove(Object o) {
		@SuppressWarnings("unchecked")
		V value = (V) o;
		return removeValue(value);
	}

	public boolean removeValue(V value) {
		return map.remove(getKey(value), value);
	}

	public V removeKey(K key) {
		return map.remove(key);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return super.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return super.retainAll(c);
	}

	@Override
	public Object[] toArray() {
		return map.values().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return map.values().toArray(a);
	}
}
