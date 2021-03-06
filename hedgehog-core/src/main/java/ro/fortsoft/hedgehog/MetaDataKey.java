/*
 * Copyright (C) 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.fortsoft.hedgehog;

/**
 * @author Jonathan Locke
 * 
 * @param <T>
 *            The type of the object which is stored
 *
 */
public abstract class MetaDataKey<T> {

	/**
	 * Constructor.
	 */
	public MetaDataKey() {
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	/**
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return obj != null && getClass().equals(obj.getClass());
	}

	/**
	 * @param metaData
	 *            Array of metadata to search
	 * @return The entry value
	 */
	@SuppressWarnings("unchecked")
	public T get(MetaDataEntry<?>[] metaData) {
		if (metaData != null) {
			for (MetaDataEntry<?> entry : metaData) {
				if (equals(entry.key)) {
					return (T) entry.object;
				}
			}
		}
		return null;
	}

	/**
	 * @param metaData
	 *            The array of metadata
	 * @param object
	 *            The object to set, null to remove
	 * @return Any new metadata array (if it was reallocated)
	 */
	public MetaDataEntry<T>[] set(MetaDataEntry<T>[] metaData, final Object object) {
		boolean set = false;
		if (metaData != null) {
			for (int i = 0; i < metaData.length; i++) {
				MetaDataEntry<?> m = metaData[i];
				if (equals(m.key)) {
					if (object != null) {
						// set new value
						m.object = object;
					} else {
						// remove value and shrink or null array
						if (metaData.length > 1) {
							int l = metaData.length - 1;
							MetaDataEntry<T>[] newMetaData = new MetaDataEntry[l];
							System.arraycopy(metaData, 0, newMetaData, 0, i);
							System.arraycopy(metaData, i + 1, newMetaData, i, l - i);
							metaData = newMetaData;
						} else {
							metaData = null;
						}
					}
					set = true;
					break;
				}
			}
		}
		if (!set && object != null) {
			MetaDataEntry<T> m = new MetaDataEntry<T>(this, object);
			if (metaData == null) {
				metaData = new MetaDataEntry[1];
				metaData[0] = m;
			} else {
				final MetaDataEntry<T>[] newMetaData = new MetaDataEntry[metaData.length + 1];
				System.arraycopy(metaData, 0, newMetaData, 0, metaData.length);
				newMetaData[metaData.length] = m;
				metaData = newMetaData;
			}
		}
		return metaData;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().toString();
	}
}
