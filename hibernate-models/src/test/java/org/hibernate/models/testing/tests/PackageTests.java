/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright: Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.models.testing.tests;

import org.hibernate.models.spi.ClassDetails;
import org.hibernate.models.spi.ModelsContext;
import org.hibernate.models.testing.annotations.pkg.PackageAnnotation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.models.testing.TestHelper.createModelContext;

/**
 * @author Steve Ebersole
 */
public class PackageTests {
	private static final String PACKAGE_NAME = PackageAnnotation.class.getPackageName();

	@Test
	void testExactReference() {
		final ModelsContext modelsContext = createModelContext();
		final String packageInfoName = PACKAGE_NAME + ".package-info";
		final ClassDetails classDetails = modelsContext
				.getClassDetailsRegistry()
				.resolveClassDetails( packageInfoName );
		assertThat( classDetails ).isNotNull();
		assertThat( classDetails.getClassName() ).endsWith( "package-info" );
		assertThat( classDetails.getAnnotationUsage( PackageAnnotation.class, modelsContext ) ).isNotNull();
	}

	@Test
	void testPackageReference() {
		final ModelsContext modelsContext = createModelContext();
		final ClassDetails classDetails = modelsContext
				.getClassDetailsRegistry()
				.resolveClassDetails( PACKAGE_NAME );
		assertThat( classDetails ).isNotNull();
		assertThat( classDetails.getClassName() ).endsWith( "package-info" );
		assertThat( classDetails.getAnnotationUsage( PackageAnnotation.class, modelsContext ) ).isNotNull();
	}

	@Test
	void testGetPackageFromClassWithPackageInfo() {
		final ModelsContext modelsContext = createModelContext( PackageAnnotation.class );
		final ClassDetails classDetails = modelsContext
				.getClassDetailsRegistry()
				.resolveClassDetails( PackageAnnotation.class.getName() );
		final ClassDetails pkg = classDetails.getPackage();
		assertThat( pkg ).isNotNull();
		assertThat( pkg.getClassName() ).isEqualTo( PACKAGE_NAME + ".package-info" );
		assertThat( pkg.getAnnotationUsage( PackageAnnotation.class, modelsContext ) ).isNotNull();
	}

	@Test
	void testGetPackageFromClassWithoutPackageInfo() {
		final ModelsContext modelsContext = createModelContext( PackageTests.class );
		final ClassDetails classDetails = modelsContext
				.getClassDetailsRegistry()
				.resolveClassDetails( PackageTests.class.getName() );
		final ClassDetails pkg = classDetails.getPackage();
		assertThat( pkg ).isNotNull();
		assertThat( pkg.getClassName() ).endsWith( "package-info" );
	}

	@Test
	void testGetPackageFromDefaultPackageClass() {
		assertThat( ClassDetails.VOID_CLASS_DETAILS.getPackage() ).isNull();
	}

	@Test
	void testGetPackageFromPackageInfo() {
		final ModelsContext modelsContext = createModelContext();
		final String packageInfoName = PACKAGE_NAME + ".package-info";
		final ClassDetails packageInfoDetails = modelsContext
				.getClassDetailsRegistry()
				.resolveClassDetails( packageInfoName );
		final ClassDetails parentPkg = packageInfoDetails.getPackage();
		if ( parentPkg != null ) {
			assertThat( parentPkg.getClassName() )
					.isEqualTo( "org.hibernate.models.testing.annotations.package-info" );
		}
	}
}
