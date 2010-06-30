/*
 * 
 */
package mm_hybrid.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;

/**
 * @generated
 */
public class SourceElementRuleConditionItemSemanticEditPolicy extends
		mm_hybrid.diagram.edit.policies.MM_HybridBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	protected Command getCreateCommand(CreateElementRequest req) {
		if (mm_hybrid.diagram.providers.MM_HybridElementTypes.Condition_3002 == req
				.getElementType()) {
			if (req.getContainmentFeature() == null) {
				req.setContainmentFeature(mm_hybrid.MM_HybridPackage.eINSTANCE
						.getSourceElementRule_Condition());
			}
			return getGEFWrapper(new mm_hybrid.diagram.edit.commands.ConditionCreateCommand(
					req));
		}
		return super.getCreateCommand(req);
	}

}
