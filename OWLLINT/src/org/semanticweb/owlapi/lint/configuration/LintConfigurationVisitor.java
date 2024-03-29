/**
 * 
 */
package org.semanticweb.owlapi.lint.configuration;

import uk.ac.manchester.cs.owl.lint.commons.NonConfigurableLintConfiguration;

/**
 * Visitor that walks the hierarchy of configuration aspects of a Lint
 * 
 * @author Luigi Iannone
 * 
 */
public interface LintConfigurationVisitor {
	void visitNonConfigurableLintConfiguration(
			NonConfigurableLintConfiguration nonConfigurableLintConfiguration);

	void visitPropertiesBasedLintConfiguration(
			PropertyBasedLintConfiguration propertiesBasedLintConfiguration);

	void visitGenericLintConfiguration(LintConfiguration lintConfiguration);
}
