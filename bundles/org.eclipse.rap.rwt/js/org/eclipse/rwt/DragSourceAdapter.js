/*******************************************************************************
 * Copyright (c) 2011, 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/

org.eclipse.rwt.protocol.AdapterRegistry.add( "rwt.widgets.DragSource", {

  factory : function( properties ) {
    var control = org.eclipse.rwt.protocol.ObjectManager.getObject( properties.control );
    var result = { "control" : control };
    org.eclipse.rwt.DNDSupport.getInstance().registerDragSource( control, properties.style );
    return result;
  },
  
  destructor : function( source ) {
    org.eclipse.rwt.DNDSupport.getInstance().deregisterDragSource( source.control );
  },

  properties : [ "transfer" ],

  propertyHandler : {
    "transfer" : function( source, value ) {
      var control = source.control;
      org.eclipse.rwt.DNDSupport.getInstance().setDragSourceTransferTypes( control, value );
    }
  },

  methods : [ "changeFeedback", "changeDetail", "changeDataType", "cancel" ],
  
  methodHandler : {
    "changeFeedback" : function( source, properties ) {
      var dnd = org.eclipse.rwt.DNDSupport.getInstance();
      var control = org.eclipse.rwt.protocol.ObjectManager.getObject( properties.control );
      var feedback = properties.feedback;
      var flags = properties.flags;
      dnd.setFeedback( control, feedback, flags );
    },
    "changeDetail" : function( source, properties ) {
      var dnd = org.eclipse.rwt.DNDSupport.getInstance();
      var control = org.eclipse.rwt.protocol.ObjectManager.getObject( properties.control );
      var detail = properties.detail;
      dnd.setOperationOverwrite( control, detail );
    },
    "changeDataType" : function( source, properties ) {
      var dnd = org.eclipse.rwt.DNDSupport.getInstance();
      var control = org.eclipse.rwt.protocol.ObjectManager.getObject( properties.control );
      var dataType = properties.dataType;
      dnd.setDataType( control, dataType );
    },
    "cancel" : function( source, properties ) {
      var dnd = org.eclipse.rwt.DNDSupport.getInstance();
      dnd.cancel();
    }
  }

} );