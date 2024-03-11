/**
 * 
 */
package com.xkyss.jsql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <b>扩展查询条件泛型类</b><br>
 * 实现以样本对象的方式，定义复杂的查询条件。<br>
 * 支持指定范围查询（大于等于和小于等于）和模糊查询（like查询）
 * @author rechard
 *
 */
public class ExtQueryConditions<T> {
	private static final long serialVersionUID = 439648460486245336L;

	/**
	 * <b>查询范围样本</b>
	 */
	public static class RangeExample<T> {
		/**
		 * 下限样本
		 */
		private T lowerLimitExample;
		/**
		 * 上限样本
		 */
		private T upperLimitExample;
		/**
		 * 上限样本
		 */
		public T getUpperLimitExample() {
			return upperLimitExample;
		}
		/**
		 * 上限样本
		 */
		public void setUpperLimitExample(T upperLimitExample) {
			this.upperLimitExample = upperLimitExample;
		}
		/**
		 * 下限样本
		 */
		public T getLowerLimitExample() {
			return lowerLimitExample;
		}
		/**
		 * 下限样本
		 */
		public void setLowerLimitExample(T lowerLimitExample) {
			this.lowerLimitExample = lowerLimitExample;
		}
		public RangeExample(){
		}
		/**
		 * @param lowerLimitExample 下限样本
		 * @param upperLimitExample 上限样本
		 */
		public RangeExample(T lowerLimitExample, T upperLimitExample) {
			this.lowerLimitExample = lowerLimitExample;
			this.upperLimitExample = upperLimitExample;
		}
	}
	/**
	 * 范围查询样本对象集
	 */
	private List<RangeExample<T>> rangeExamples;
	/**
	 * 模糊查询样本对象集
	 */
	private List<T> likeExamples;
	/**
	 * 作为IN条件的值集<br>
	 * IN条件仅支持T对象主键
	 */
	private List<Object> inValues;
	/**
	 * 作为NOT IN条件的值集<br>
	 * NOT IN条件仅支持T对象主键
	 */
	private List<Object> notInValues;
	/**
	 * 作为IN条件的值集<br>
	 * IN条件仅支持T对象主键
	 */
	public List<Object> getInValues() {
		return inValues;
	}
	/**
	 * 作为IN条件的值集<br>
	 * IN条件仅支持T对象主键
	 */
	public void setInValues(List<Object> inValues) {
		this.inValues = inValues;
	}
	/**
	 * 作为NOT IN条件的值集<br>
	 * NOT IN条件仅支持T对象主键
	 */
	public List<Object> getNotInValues() {
		return notInValues;
	}
	/**
	 * 作为NOT IN条件的值集<br>
	 * NOT IN条件仅支持T对象主键
	 */
	public void setNotInValues(List<Object> notInValues) {
		this.notInValues = notInValues;
	}
	/**
	 * 范围查询样本对象集
	 */
	public List<RangeExample<T>> getRangeExamples() {
		if (this.rangeExamples == null) {
			this.rangeExamples = new ArrayList<RangeExample<T>>();
		}
		return rangeExamples;
	}
	/**
	 * 范围查询样本对象集
	 */
	public void setRangeExamples(List<RangeExample<T>> rangeExamples) {
		this.rangeExamples = rangeExamples;
	}
	/**
	 * 模糊查询样本对象集
	 */
	public List<T> getLikeExamples() {
		if (this.likeExamples == null) {
			this.likeExamples = new ArrayList<T>();
		}
		return likeExamples;
	}
	/**
	 * 模糊查询样本对象集
	 */
	public void setLikeExamples(List<T> likeExamples) {
		this.likeExamples = likeExamples;
	}

	/**
	 * 
	 */
	public ExtQueryConditions() {
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ExtQueryConditions<T> clone() {
		ExtQueryConditions<T> co = new ExtQueryConditions<T>();
		co.setInValues(this.getInValues());
		co.setLikeExamples(this.getLikeExamples());
		co.setNotInValues(this.getNotInValues());
		co.setRangeExamples(this.getRangeExamples());
		return co;
	}
	/**
	 * 吸收另外一个扩展查询条件对象的特性到当前对象
	 * @param eqc 要吸收的扩展查询对象
	 * @return 当前对象
	 */
	public ExtQueryConditions<T> Absorption(ExtQueryConditions<T> eqc) {
		if (this.getInValues() != null) {
			if (eqc.getInValues() != null) {
				this.getInValues().addAll(eqc.getInValues());
			}
		} else {
			this.setInValues(eqc.getInValues());
		}
		
		if (this.getLikeExamples() != null) {
			if (eqc.getLikeExamples() != null) {
				this.getLikeExamples().addAll(eqc.getLikeExamples());
			}
		} else {
			this.setLikeExamples(eqc.getLikeExamples());
		}
		
		if (this.getNotInValues() != null) {
			if (eqc.getNotInValues() != null) {
				this.getNotInValues().addAll(eqc.getNotInValues());
			}
		} else {
			this.setNotInValues(eqc.getNotInValues());
		}
		
		if (this.getRangeExamples() != null) {
			if (eqc.getRangeExamples() != null) {
				this.getRangeExamples().addAll(eqc.getRangeExamples());
			}
		} else {
			this.setRangeExamples(eqc.getRangeExamples());
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.aj.frame.beans.AJFrameBean#importantFieldValues()
	 */
	// @Override
	// public String[] importantFieldValues() {
	// 	return new String[] {this.likeExamples == null ? "0" : "" + this.likeExamples.size(), this.rangeExamples == null ? "0" : "" + this.rangeExamples.size()};
	// }

	/**
	 * 在当前扩展查询条件对象中添加一个下限样本<br>
	 * @param lowerLimitExample 查询范围下限样本对象
	 * @return 当前扩展查询条件对象
	 */
	public ExtQueryConditions<T> addLowerLimitExample(T lowerLimitExample) {
		this.getRangeExamples().add(new RangeExample<T>(lowerLimitExample, null));
		return this;
	}
	/**
	 * 在当前扩展查询条件对象中添加一个上限样本<br>
	 * @param upperLimitExample 查询范围上限样本对象
	 * @return 当前扩展查询条件对象
	 */
	public ExtQueryConditions<T> addUpperLimitExample(T upperLimitExample) {
		this.getRangeExamples().add(new RangeExample<T>(null, upperLimitExample));
		return this;
	}
	/**
	 * 在当前扩展查询条件对象中添加一对范围样本<br>
	 * @param lowerLimitExample 查询范围下限样本对象
	 * @param upperLimitExample 查询范围上限样本对象
	 * @return 当前扩展查询条件对象
	 */
	public ExtQueryConditions<T> addRangeExample(T lowerLimitExample, T upperLimitExample) {
		this.getRangeExamples().add(new RangeExample<T>(lowerLimitExample, upperLimitExample));
		return this;
	}
	/**
	 * 在当前扩展查询条件对象中添加一个IN条件值集<br>
	 * @param inValues IN条件值集
	 * @return 当前扩展查询条件对象
	 */
	public ExtQueryConditions<T> addInValues(List<Object> inValues) {
		if (this.getInValues() == null) {
			this.setInValues(inValues);
		} else {
			this.getInValues().addAll(inValues);
		}
		return this;
	}
	/**
	 * 在当前扩展查询条件对象中添加一个IN条件值<br>
	 * @param inValue IN条件值
	 * @return 当前扩展查询条件对象
	 */
	public ExtQueryConditions<T> addInValue(Object inValue) {
		if (this.getInValues() == null) {
			this.setInValues(new ArrayList<Object>());
		}
		this.getInValues().add(inValue);
		return this;
	}
	/**
	 * 在当前扩展查询条件对象中添加一个NOT IN条件值集<br>
	 * @param notInValues NOT IN条件值集
	 * @return 当前扩展查询条件对象
	 */
	public ExtQueryConditions<T> addNotInValues(List<Object> notInValues) {
		if (this.getNotInValues() == null) {
			this.setNotInValues(notInValues);
		} else {
			this.getNotInValues().addAll(notInValues);
		}
		return this;
	}
	/**
	 * 在当前扩展查询条件对象中添加一个NOT IN条件值<br>
	 * @param notInValue NOT IN条件值
	 * @return 当前扩展查询条件对象
	 */
	public ExtQueryConditions<T> addNotInValue(Object notInValue) {
		if (this.getNotInValues() == null) {
			this.setNotInValues(new ArrayList<Object>());
		}
		this.getNotInValues().add(notInValue);
		return this;
	}
	/**
	 * 在当前扩展查询条件对象中添加一个LIKE样本<br>
	 * @param likeExample LIKE查询样本对象
	 * @return 当前扩展查询条件对象
	 */
	public ExtQueryConditions<T> addLikeExample(T likeExample) {
		this.getLikeExamples().add(likeExample);
		return this;
	}
	/**
	 * 获取当前扩展查询对象支持的映射类对象<br>
	 * 由于泛型对象在实例对象类型明确之前无法无法获取类型信息，因此该方法通过查找内部对象的方式，获得明确的实例对象类型。
	 * @return 当前扩展查询对象支持的映射类对象
	 */
	public Class<?> examplesClass() {
		Class<?> cls = null;
		Object firstExample = null;
		if (this.rangeExamples != null && this.rangeExamples.size() > 0) {
			Iterator<RangeExample<T>> reit = this.rangeExamples.iterator();
			while (reit.hasNext() && firstExample == null) {
				RangeExample<T> range = reit.next();
				firstExample = range.getLowerLimitExample();
				if (firstExample == null) {
					firstExample = range.getUpperLimitExample();
				}
			}
		}
		if (firstExample == null && this.likeExamples != null && this.likeExamples.size() > 0) {
			Iterator<T> lkit = this.likeExamples.iterator();
			while (lkit.hasNext() && firstExample == null) {
				firstExample = lkit.next();
			}
		}
		if (firstExample != null) {
			cls = firstExample.getClass();
		}
		return cls;
	}
}
