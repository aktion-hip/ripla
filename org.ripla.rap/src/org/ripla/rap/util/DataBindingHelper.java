/*******************************************************************************
 * Copyright (c) 2013 RelationWare, Benno Luthiger
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * RelationWare, Benno Luthiger
 ******************************************************************************/
package org.ripla.rap.util;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Widget;

/**
 * Wrapper for DataBindingContext. This helper class provides convenience
 * methods for binding model fields to form input widgets.
 * <p>
 * Usage examples:
 * 
 * <pre>
 * DataBindingHelper ctx = new DataBindingHelper(MyModel.class, myModel);
 * ctx.bindValue(ctx.getWidgetToOserve(WidgetProp.TEXT, firstName),
 * 		ctx.getModelToObserve(&quot;firstName&quot;));
 * </pre>
 * 
 * to bind the text input field "firstName" to the <code>firstName</code> field
 * of the MyModel instance "myModel".
 * </p>
 * 
 * @author Luthiger
 * @see AbstractRiplaForm
 */
public class DataBindingHelper {

	private final DataBindingContext context;
	private final Class<?> modelCls;
	private final Object model;

	/**
	 * Enum for meaningful <code>IWidgetValueProperty</code> instances.
	 */
	public enum WidgetProp {
		TEXT(WidgetProperties.text(SWT.Modify)), SELECTION(WidgetProperties
				.selection());

		private IWidgetValueProperty prop;

		WidgetProp(final IWidgetValueProperty inProp) {
			prop = inProp;
		}

		public IWidgetValueProperty get() {
			return prop;
		}
	}

	/**
	 * DataBindingHelper constructor.
	 * 
	 * @param inModelClass
	 *            Class the model (i.e. bean) class.
	 * @param inModelInstance
	 *            Object the model (i.e. bean) instance.
	 * 
	 */
	public DataBindingHelper(final Class<?> inModelClass,
			final Object inModelInstance) {
		context = new DataBindingContext();
		modelCls = inModelClass;
		model = inModelInstance;
	}

	/**
	 * Convenience method to create a target observable.
	 * 
	 * @param inProperty
	 *            {@link WidgetProp} the property to create the observable
	 * @param inWidget
	 *            {@link Widget} the widget that is observed
	 * @return ISWTObservableValue
	 */
	public ISWTObservableValue getWidgetToOserve(final WidgetProp inProperty,
			final Widget inWidget) {
		return inProperty.get().observe(inWidget);
	}

	/**
	 * Convenience method to create a target observable.
	 * 
	 * @param inModelProperty
	 *            String the name of the model property to be observed.
	 * @return IObservableValue
	 */
	public IObservableValue getModelToObserve(final String inModelProperty) {
		return BeanProperties.value(modelCls, inModelProperty).observe(model);
	}

	/**
	 * Creates a Binding to synchronize the values of two observable values.
	 * 
	 * @param inTargetObservable
	 *            {@link IObservableValue}
	 * @param inModelObservable
	 *            {@link IObservableValue}
	 * @return {@link Binding}
	 */
	public final Binding bindValue(final IObservableValue inTargetObservable,
			final IObservableValue inModelObservable) {
		return context.bindValue(inTargetObservable, inModelObservable, null,
				null);
	}

	/**
	 * Creates a Binding to synchronize the values of two observable values.
	 * 
	 * @param inTargetObservable
	 *            {@link IObservableValue}
	 * @param inModelObservable
	 *            {@link IObservableValue}
	 * @param inStrategy
	 *            {@link UpdateValueStrategy} a strategy to employ when the
	 *            target is the source of the change and the model is the
	 *            destination
	 * @return {@link Binding}
	 */
	public final Binding bindValue(final IObservableValue inTargetObservable,
			final IObservableValue inModelObservable,
			final UpdateValueStrategy inStrategy) {
		return context.bindValue(inTargetObservable, inModelObservable,
				inStrategy, null);
	}

	/**
	 * Creates a Binding to synchronize the values of two observable values.
	 * Decorates the input field to mark it as required.
	 * 
	 * @param inTargetObservable
	 *            {@link IObservableValue}
	 * @param inModelObservable
	 *            {@link IObservableValue}
	 * @param inStrategy
	 *            {@link UpdateValueStrategy} a strategy to employ when the
	 *            target is the source of the change and the model is the
	 *            destination
	 * @return {@link Binding}
	 */
	public final Binding bindValueRequired(
			final IObservableValue inTargetObservable,
			final IObservableValue inModelObservable,
			final UpdateValueStrategy inStrategy) {
		final Binding out = context.bindValue(inTargetObservable,
				inModelObservable, inStrategy, null);
		ControlDecorationSupport.create(out, SWT.TOP | SWT.LEFT);
		return out;
	}

	/**
	 * Creates a Binding to synchronize the values of two observable values.
	 * 
	 * @param inTargetObservable
	 *            {@link IObservableValue}
	 * @param inModelObservable
	 *            {@link IObservableValue}
	 * @param inTargetToModel
	 *            {@link UpdateValueStrategy} a strategy to employ when the
	 *            target is the source of the change and the model is the
	 *            destination
	 * @param inModelToTarget
	 *            {@link UpdateValueStrategy} a strategy to employ when the
	 *            model is the source of the change and the target is the
	 *            destination
	 * @return {@link Binding}
	 */
	public final Binding bindValue(final IObservableValue inTargetObservable,
			final IObservableValue inModelObservable,
			final UpdateValueStrategy inTargetToModel,
			final UpdateValueStrategy inModelToTarget) {
		return context.bindValue(inTargetObservable, inModelObservable,
				inTargetToModel, inModelToTarget);
	}

	/**
	 * Creates a Binding to synchronize the values of two observable values.
	 * Decorates the input field to mark it as required.
	 * 
	 * @param inTargetObservable
	 *            {@link IObservableValue}
	 * @param inModelObservable
	 *            {@link IObservableValue}
	 * @param inTargetToModel
	 *            {@link UpdateValueStrategy} a strategy to employ when the
	 *            target is the source of the change and the model is the
	 *            destination
	 * @param inModelToTarget
	 *            {@link UpdateValueStrategy} a strategy to employ when the
	 *            model is the source of the change and the target is the
	 *            destination
	 * @return {@link Binding}
	 */
	public final Binding bindValueRequired(
			final IObservableValue inTargetObservable,
			final IObservableValue inModelObservable,
			final UpdateValueStrategy inTargetToModel,
			final UpdateValueStrategy inModelToTarget) {
		final Binding out = context.bindValue(inTargetObservable,
				inModelObservable, inTargetToModel, inModelToTarget);
		ControlDecorationSupport.create(out, SWT.TOP | SWT.LEFT);
		return out;

	}

	/**
	 * Checks the aggregated input.
	 * 
	 * @return boolean <code>true</code> if all validations passed, else
	 *         <code>false</code>.
	 */
	public boolean checkStatus() {
		final AggregateValidationStatus lStatus = new AggregateValidationStatus(
				context, AggregateValidationStatus.MAX_SEVERITY);
		final Object lValue = lStatus.getValue();
		if (lValue instanceof IStatus) {
			return ((IStatus) lValue).isOK();
		}
		return true;
	}

	/**
	 * Get all status messages as an html list (ul).
	 * 
	 * @return String the status messages
	 */
	public String getStatusMsg() {
		final AggregateValidationStatus lStatus = new AggregateValidationStatus(
				context, AggregateValidationStatus.MERGED);
		final Object lValue = lStatus.getValue();
		if (lValue instanceof IStatus) {
			final IStatus lStatusVal = (IStatus) lValue;
			if (lStatusVal.isMultiStatus()) {
				final StringBuilder out = new StringBuilder("<ul>");
				for (final IStatus lChild : lStatusVal.getChildren()) {
					out.append("<li>").append(lChild.getMessage())
							.append("</li>");
				}
				out.append("</ul>");
				return new String(out);
			} else {
				return String.format("<p>%s</p>", lStatusVal.getMessage());
			}
		}
		return "";
	}

	/**
	 * Create a strategy using the specified validator before the value is set
	 * (to the model).
	 * 
	 * @param inValidator
	 *            {@link IValidator} the validator to check in input value
	 * @return {@link UpdateValueStrategy}
	 */
	public UpdateValueStrategy createStrategyBeforeSet(
			final IValidator inValidator) {
		final UpdateValueStrategy out = new UpdateValueStrategy();
		out.setBeforeSetValidator(inValidator);
		return out;
	}

	/**
	 * Create a strategy using the specified validator after the value is
	 * entered (i.e. before conversion).
	 * 
	 * @param inValidator
	 *            {@link IValidator} the validator to check in input value
	 * @return {@link UpdateValueStrategy}
	 */
	public UpdateValueStrategy createStrategyAfterGet(
			final IValidator inValidator) {
		final UpdateValueStrategy out = new UpdateValueStrategy();
		out.setAfterGetValidator(inValidator);
		return out;
	}

	/**
	 * Create a strategy using the specified validator after the value is
	 * converted.
	 * 
	 * @param inValidator
	 *            {@link IValidator} the validator to check in input value
	 * @return {@link UpdateValueStrategy}
	 */
	public UpdateValueStrategy createStrategyAfterConvert(
			final IValidator inValidator) {
		final UpdateValueStrategy out = new UpdateValueStrategy();
		out.setAfterConvertValidator(inValidator);
		return out;
	}

}
