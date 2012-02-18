/*******************************************************************************
 * Copyright (c) 2002, 2012 Innoopract Informationssysteme GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Innoopract Informationssysteme GmbH - initial API and implementation
 *    EclipseSource - ongoing development
 ******************************************************************************/
package org.eclipse.rwt.internal.lifecycle;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import junit.framework.TestCase;

import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.rwt.lifecycle.DefaultEntryPointFactory;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.rwt.lifecycle.IEntryPointFactory;


public class EntryPointManager_Test extends TestCase {

  private static final String NAME = "entryPointName";
  private static final String PATH = "/entrypoint";
  private static final Integer RETURN_VALUE = Integer.valueOf( 123 );

  private EntryPointManager entryPointManager;
  private IEntryPointFactory entryPointFactory;
  private IEntryPoint entryPoint;

  public static class TestEntryPoint implements IEntryPoint {
    public int createUI() {
      return 0;
    }
  }

  @Override
  protected void setUp() throws Exception {
    Fixture.setUp();
    mockEntryPoint();
    mockEntryPointFactory();
    entryPointManager = new EntryPointManager();
  }

  private void mockEntryPointFactory() {
    entryPointFactory = mock( IEntryPointFactory.class );
    when( entryPointFactory.create() ).thenReturn( entryPoint );
  }

  private void mockEntryPoint() {
    entryPoint = mock( IEntryPoint.class );
    when( Integer.valueOf( entryPoint.createUI() ) ).thenReturn( RETURN_VALUE );
  }

  @Override
  protected void tearDown() throws Exception {
    Fixture.tearDown();
  }

  public void testRegisterEntryPointByPath_nullPath() {
    try {
      entryPointManager.registerByPath( null, TestEntryPoint.class );
      fail();
    } catch( NullPointerException expected ) {
    }
  }

  public void testRegisterEntryPointByPath_nullClass() {
    try {
      entryPointManager.registerByPath( PATH, ( Class<? extends IEntryPoint> )null );
      fail();
    } catch( NullPointerException expected ) {
    }
  }

  public void testRegisterEntryPointByPath_duplicate() {
    entryPointManager.registerByPath( PATH, TestEntryPoint.class );
    try {
      entryPointManager.registerByPath( PATH, TestEntryPoint.class );
      fail();
    } catch( IllegalArgumentException expected ) {
    }
  }

  public void testRegisterEntryPointByPath_illegalPath() {
    Class<TestEntryPoint> entryPointClass = TestEntryPoint.class;
    assertRegisterByPathFails( "", entryPointClass );
    assertRegisterByPathFails( "/", entryPointClass );
    assertRegisterByPathFails( "foo", entryPointClass );
    assertRegisterByPathFails( "/foo/", entryPointClass );
    assertRegisterByPathFails( "/foo/bar", entryPointClass );
  }

  public void testRegisterEntryPointByPath() {
    entryPointManager.registerByPath( PATH, TestEntryPoint.class );

    IEntryPointFactory factory = entryPointManager.getFactoryByPath( PATH );

    assertEquals( DefaultEntryPointFactory.class, factory.getClass() );
    assertEquals( TestEntryPoint.class, factory.create().getClass() );
  }

  public void testRegisterFactoryByPath_nullPath() {
    try {
      entryPointManager.registerByPath( null, entryPointFactory );
      fail();
    } catch( NullPointerException expected ) {
    }
  }

  public void testRegisterFactoryByPath_duplicate() {
    entryPointManager.registerByPath( PATH, entryPointFactory );
    try {
      entryPointManager.registerByPath( PATH, entryPointFactory );
      fail();
    } catch( IllegalArgumentException expected ) {
    }
  }

  public void testRegisterFactoryByPath_illegalPath() {
    assertRegisterByPathFails( "", entryPointFactory );
    assertRegisterByPathFails( "/", entryPointFactory );
    assertRegisterByPathFails( "foo", entryPointFactory );
    assertRegisterByPathFails( "/foo/", entryPointFactory );
    assertRegisterByPathFails( "/foo/bar", entryPointFactory );
  }

  public void testRegisterFactoryByPath_nullFactory() {
    try {
      entryPointManager.registerByPath( PATH, ( IEntryPointFactory )null );
      fail();
    } catch( NullPointerException expected ) {
    }
  }

  public void testRegisterFactoryByPath() {
    entryPointManager.registerByPath( PATH, entryPointFactory );

    IEntryPointFactory factory = entryPointManager.getFactoryByPath( PATH );

    assertSame( entryPointFactory, factory );
  }

  public void testGetFactoryByPath_nonExisting() {
    assertNull( entryPointManager.getFactoryByPath( PATH ) );
  }

  public void testGetServletPaths_initallyEmpty() {
    assertTrue( entryPointManager.getServletPaths().isEmpty() );
  }

  public void testGetServletPaths() {
    entryPointManager.registerByPath( "/foo", entryPointFactory );
    entryPointManager.registerByPath( "/bar", entryPointFactory );

    assertEquals( 2, entryPointManager.getServletPaths().size() );
    assertTrue( entryPointManager.getServletPaths().contains( "/foo" ) );
    assertTrue( entryPointManager.getServletPaths().contains( "/bar" ) );
  }

  public void testRegisterEntryPointByName_nullName() {
    try {
      entryPointManager.registerByName( null, TestEntryPoint.class );
      fail();
    } catch( NullPointerException expected ) {
    }
  }

  public void testRegisterEntryPointByName_nullClass() {
    try {
      entryPointManager.registerByName( NAME, ( Class<? extends IEntryPoint> )null );
      fail();
    } catch( NullPointerException expected ) {
    }
  }

  public void testRegisterEntryPointByName_duplicate() {
    entryPointManager.registerByName( NAME, TestEntryPoint.class );
    try {
      entryPointManager.registerByName( NAME, TestEntryPoint.class );
      fail();
    } catch( IllegalArgumentException expected ) {
    }
  }

  public void testRegisterEntryPointByName() {
    entryPointManager.registerByName( NAME, TestEntryPoint.class );

    IEntryPointFactory factory = entryPointManager.getFactoryByName( NAME );

    assertEquals( DefaultEntryPointFactory.class, factory.getClass() );
    assertEquals( TestEntryPoint.class, factory.create().getClass() );
  }

  public void testRegisterFactoryByName_nullPath() {
    try {
      entryPointManager.registerByName( null, entryPointFactory );
      fail();
    } catch( NullPointerException expected ) {
    }
  }

  public void testRegisterFactoryByName_nullFactory() {
    try {
      entryPointManager.registerByName( NAME, ( IEntryPointFactory )null );
      fail();
    } catch( NullPointerException expected ) {
    }
  }

  public void testRegisterFactoryByName() {
    entryPointManager.registerByName( NAME, entryPointFactory );

    IEntryPointFactory factory = entryPointManager.getFactoryByName( NAME );

    assertSame( entryPointFactory, factory );
  }

  public void testGetFactoryByName_nonExisting() {
    assertNull( entryPointManager.getFactoryByName( NAME ) );
  }

  public void testDeregisterAll() {
    entryPointManager.registerByPath( PATH, entryPointFactory );
    entryPointManager.registerByName( NAME, TestEntryPoint.class );
    entryPointManager.deregisterAll();

    assertTrue( entryPointManager.getServletPaths().isEmpty() );
    assertNull( entryPointManager.getFactoryByPath( NAME ) );
    assertNull( entryPointManager.getFactoryByName( NAME ) );
  }

  private void assertRegisterByPathFails( String path, Class<? extends IEntryPoint> type ) {
    try {
      entryPointManager.registerByPath( path, type );
      fail( "Exected to fail but succeeded" );
    } catch( IllegalArgumentException expected ) {
    }
  }

  private void assertRegisterByPathFails( String path, IEntryPointFactory factory ) {
    try {
      entryPointManager.registerByPath( path, factory );
      fail( "Exected to fail but succeeded" );
    } catch( IllegalArgumentException expected ) {
    }
  }
}
