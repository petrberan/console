$wnd.hal.runAsyncCallback51("defineClass(1040, 1, {1:1});\n_.com$gwtplatform$mvp$client$HandlerContainerImpl_automaticBind_methodInjection________________________________________ = function com$gwtplatform$mvp$client$HandlerContainerImpl_automaticBind_methodInjection________________________________________(invokee, _0){\n  invokee.automaticBind(_0);\n}\n;\nfunction $clinit_PreElement(){\n  $clinit_PreElement = emptyMethod;\n}\n\nvar Lelemental_html_PreElement_2_classLit = createForInterface('elemental.html', 'PreElement');\nfunction $createPreElement(this$static){\n  $clinit_JsDocument();\n  return $createPreElement_0(this$static);\n}\n\nfunction $createPreElement_0(this$static){\n  return $cast($createElement_0(this$static, 'pre'));\n}\n\nfunction $clinit_JsPreElement(){\n  $clinit_JsPreElement = emptyMethod;\n  $clinit_JsElement();\n}\n\nfunction $clinit_ServerStatusPresenter(){\n  $clinit_ServerStatusPresenter = emptyMethod;\n  $clinit_ApplicationFinderPresenter();\n  SERVER_STATUS_TEMPLATE = of_14('/{selected.host}/{selected.server}/core-service=platform-mbean/type=runtime');\n}\n\nfunction ServerStatusPresenter(eventBus, view, myProxy, finder, finderPathFactory, dispatcher, statementContext, resources){\n  $clinit_ServerStatusPresenter();\n  ApplicationFinderPresenter.call(this, eventBus, view, myProxy, finder);\n  this.$init_1321();\n  this.finderPathFactory = finderPathFactory;\n  this.dispatcher = dispatcher;\n  this.statementContext = statementContext;\n  this.resources = resources;\n}\n\ndefineClass(1364, 155, {58:1, 45:1, 1:1, 28:1, 27:1, 97:1}, ServerStatusPresenter);\n_.$init_1321 = function $init_1321(){\n}\n;\n_.lambda$0_120 = function lambda$0_217(result_0){\n  $clinit_ServerStatusPresenter();\n  castTo(this.getView(), 4305).update_8(result_0);\n}\n;\n_.finderPath = function finderPath_28(){\n  return this.finderPathFactory.runtimeServerPath().append_19('server-monitor', asId(this.resources.constants_0().status_2()), this.resources.constants_0().monitor(), this.resources.constants_0().status_2());\n}\n;\n_.reload_0 = function reload_29(){\n  var address, operation;\n  address = SERVER_STATUS_TEMPLATE.resolve_3(this.statementContext, stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), {4:1, 1:1, 5:1, 6:1}, 2, 6, []));\n  operation = (new Operation$Builder('read-resource', address)).param_2('include-runtime', true).build_17();\n  this.dispatcher.execute_15(operation, new ServerStatusPresenter$lambda$0$Type(this));\n}\n;\nvar SERVER_STATUS_TEMPLATE;\nvar Lorg_jboss_hal_client_runtime_server_ServerStatusPresenter_2_classLit = createForClass('org.jboss.hal.client.runtime.server', 'ServerStatusPresenter', 1364, Lorg_jboss_hal_core_mvp_ApplicationFinderPresenter_2_classLit);\nfunction $clinit_ServerStatusPresenter$MyView(){\n  $clinit_ServerStatusPresenter$MyView = emptyMethod;\n}\n\nvar Lorg_jboss_hal_client_runtime_server_ServerStatusPresenter$MyView_2_classLit = createForInterface('org.jboss.hal.client.runtime.server', 'ServerStatusPresenter/MyView');\nfunction $clinit_ServerStatusPresenter$lambda$0$Type(){\n  $clinit_ServerStatusPresenter$lambda$0$Type = emptyMethod;\n}\n\nfunction ServerStatusPresenter$lambda$0$Type($$outer_0){\n  $clinit_ServerStatusPresenter$lambda$0$Type();\n  this.$$outer_0 = $$outer_0;\n}\n\ndefineClass(1365, 1, {1:1, 31:1}, ServerStatusPresenter$lambda$0$Type);\n_.onSuccess_0 = function onSuccess_214(arg0){\n  this.$$outer_0.lambda$0_120(arg0);\n}\n;\nvar Lorg_jboss_hal_client_runtime_server_ServerStatusPresenter$lambda$0$Type_2_classLit = createForClass('org.jboss.hal.client.runtime.server', 'ServerStatusPresenter/lambda$0$Type', 1365, Ljava_lang_Object_2_classLit);\nfunction $clinit_ServerStatusView(){\n  $clinit_ServerStatusView = emptyMethod;\n  $clinit_HalViewImpl();\n  $clinit_HalView();\n  MAIN_ATTRIBUTES = stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), {4:1, 1:1, 5:1, 6:1}, 2, 6, ['name', 'vm-name', 'vm-vendor', 'vm-version', 'spec-name', 'spec-vendor', 'spec-version', 'management-spec-version', 'start-time', 'uptime']);\n  BOOTSTRAP_ATTRIBUTES = stampJavaTypeInfo(getClassLiteralForArray(Ljava_lang_String_2_classLit, 1), {4:1, 1:1, 5:1, 6:1}, 2, 6, ['boot-class-path-supported', 'boot-class-path', 'class-path', 'library-path', 'input-arguments']);\n}\n\nfunction ServerStatusView(metadataRegistry, resources){\n  $clinit_ServerStatusView();\n  var bootstrapSection, layoutBuilder, mainAttributesBuilder, metadata, navigation, options, root, systemPropertiesSection;\n  HalViewImpl.call(this);\n  this.$init_1325();\n  metadata = castTo(metadataRegistry.lookup_1(($clinit_ServerStatusPresenter() , SERVER_STATUS_TEMPLATE)), 23);\n  this.mainAttributes = (new ModelNodeForm$Builder(($clinit_Ids() , SERVER_STATUS_MAIN_ATTRIBUTES_FORM), metadata)).viewOnly_0().includeRuntime_0().include_7(MAIN_ATTRIBUTES).unsorted_0().build_15();\n  this.bootstrapAttributes = (new ModelNodeForm$Builder(($clinit_Ids() , SERVER_STATUS_BOOTSTRAP_FORM), metadata)).viewOnly_0().includeRuntime_0().include_7(BOOTSTRAP_ATTRIBUTES).customFormItem('boot-class-path', new ServerStatusView$lambda$0$Type).customFormItem('class-path', new ServerStatusView$lambda$1$Type).customFormItem('library-path', new ServerStatusView$lambda$2$Type).customFormItem('input-arguments', new ServerStatusView$lambda$3$Type).unsorted_0().build_15();\n  options = castTo(castTo((new OptionsBuilder_0).column_7('name', 'Name', makeLambdaFunction(ServerStatusView$lambda$4$Type.prototype.render_2, ServerStatusView$lambda$4$Type, [])), 127).column_10((new ColumnBuilder('value', 'Value', makeLambdaFunction(ServerStatusView$lambda$5$Type.prototype.render_2, ServerStatusView$lambda$5$Type, []))).width_1('66%').searchable_0(false).orderable_0(false).build_7()), 127).build_8();\n  this.systemProperties = new DataTable(($clinit_Ids() , SERVER_STATUS_SYSTEM_PROPERTIES_TABLE), options);\n  this.registerAttachable(this.mainAttributes, stampJavaTypeInfo(getClassLiteralForArray(Lorg_jboss_hal_ballroom_Attachable_2_classLit, 1), {4:1, 1:1, 5:1}, 10, 0, [this.bootstrapAttributes, this.systemProperties]));\n  mainAttributesBuilder = castTo(castTo(castTo(castTo(castTo(castTo(castTo(castTo(castTo((new Elements$Builder).section(), 3).h_1(1), 3).rememberAs('headerElement'), 3).end(), 3).p(), 3).textContent_0(metadata.getDescription_0().getDescription()), 3).end(), 3).add_12(this.mainAttributes), 3).end(), 3);\n  this.headerElement = mainAttributesBuilder.referenceFor('headerElement');\n  bootstrapSection = castTo(castTo(castTo(castTo(castTo(castTo((new Elements$Builder).section(), 3).h_1(1), 3).textContent_0('Bootstrap'), 3).end(), 3).add_12(this.bootstrapAttributes), 3).end(), 3).build_2();\n  systemPropertiesSection = castTo(castTo(castTo(castTo(castTo(castTo((new Elements$Builder).section(), 3).h_1(1), 3).textContent_0('System Properties'), 3).end(), 3).add_12(this.systemProperties), 3).end(), 3).build_2();\n  navigation = new VerticalNavigation;\n  this.registerAttachable(navigation, stampJavaTypeInfo(getClassLiteralForArray(Lorg_jboss_hal_ballroom_Attachable_2_classLit, 1), {4:1, 1:1, 5:1}, 10, 0, []));\n  navigation.addPrimary_0(($clinit_Ids() , SERVER_STATUS_MAIN_ATTRIBUTES_ENTRY), resources.constants_0().mainAttributes_0(), fontAwesome('list-ul'), mainAttributesBuilder.build_2());\n  navigation.addPrimary_0(($clinit_Ids() , SERVER_STATUS_BOOTSTRAP_ENTRY), 'Bootstrap', fontAwesome('play'), bootstrapSection);\n  navigation.addPrimary_0(($clinit_Ids() , SERVER_STATUS_SYSTEM_PROPERTIES_ENTRY), 'System Properties', pfIcon('resource-pool'), systemPropertiesSection);\n  layoutBuilder = castTo(castTo(castTo((new LayoutBuilder).row_0().column_2().addAll_0(navigation.panes_0()), 8).end(), 8).end(), 8);\n  root = layoutBuilder.build_2();\n  this.initElement(root);\n}\n\nfunction lambda$0_219(attributeDescription_0){\n  $clinit_ServerStatusView();\n  return new ServerStatusView$PreTextItem('boot-class-path');\n}\n\nfunction lambda$1_132(attributeDescription_0){\n  $clinit_ServerStatusView();\n  return new ServerStatusView$PreTextItem('class-path');\n}\n\nfunction lambda$2_105(attributeDescription_0){\n  $clinit_ServerStatusView();\n  return new ServerStatusView$PreTextItem('library-path');\n}\n\nfunction lambda$3_81(attributeDescription_0){\n  $clinit_ServerStatusView();\n  return new ServerStatusView$PreListItem('input-arguments');\n}\n\nfunction lambda$4_71(cell_0, t_1, row_2, meta_3){\n  $clinit_ServerStatusView();\n  return row_2.getName();\n}\n\nfunction lambda$5_55(cell_0, t_1, row_2, meta_3){\n  $clinit_ServerStatusView();\n  return row_2.getValue_3().asString();\n}\n\nfunction lambda$6_43(p_0){\n  $clinit_ServerStatusView();\n  return equals_Ljava_lang_Object__Z__devirtual$_0('path.separator', p_0.getName());\n}\n\nfunction lambda$7_38(p_0){\n  $clinit_ServerStatusView();\n  return p_0.getValue_3().asString();\n}\n\ndefineClass(1720, 63, {1:1, 28:1, 27:1, 4305:1, 48:1}, ServerStatusView);\n_.$init_1325 = function $init_1325(){\n}\n;\n_.pathWithNewLines = function pathWithNewLines(path, pathSeparator){\n  return join_4('\\n', on_4(pathSeparator).omitEmptyStrings_0().split_0(path));\n}\n;\n_.update_8 = function update_48(modelNode){\n  var pathSeparator, sp;\n  sp = modelNode.get_17('system-properties').asPropertyList();\n  pathSeparator = castToString(sp.stream().filter_0(new ServerStatusView$lambda$6$Type).findAny().map_2(new ServerStatusView$lambda$7$Type).orElse(':'));\n  $setTextContent(this.headerElement, modelNode.get_17('name').asString());\n  this.mainAttributes.view_0(modelNode);\n  this.mainAttributes.getFormItem('start-time').setText_0(shortDateTime(new Date_3(modelNode.get_17('start-time').asLong())));\n  this.mainAttributes.getFormItem('uptime').setText_0(humanReadableDuration(modelNode.get_17('uptime').asLong()));\n  this.bootstrapAttributes.view_0(modelNode);\n  this.bootstrapAttributes.getFormItem('boot-class-path').setText_0(this.pathWithNewLines(modelNode.get_17('boot-class-path').asString(), pathSeparator));\n  this.bootstrapAttributes.getFormItem('class-path').setText_0(this.pathWithNewLines(modelNode.get_17('class-path').asString(), pathSeparator));\n  this.bootstrapAttributes.getFormItem('library-path').setText_0(this.pathWithNewLines(modelNode.get_17('library-path').asString(), pathSeparator));\n  this.systemProperties.update_2(sp, new ServerStatusView$0methodref$getName$Type);\n}\n;\nvar BOOTSTRAP_ATTRIBUTES, MAIN_ATTRIBUTES;\nvar Lorg_jboss_hal_client_runtime_server_ServerStatusView_2_classLit = createForClass('org.jboss.hal.client.runtime.server', 'ServerStatusView', 1720, Lorg_jboss_hal_core_mvp_HalViewImpl_2_classLit);\nfunction $clinit_ServerStatusView$0methodref$getName$Type(){\n  $clinit_ServerStatusView$0methodref$getName$Type = emptyMethod;\n}\n\nfunction ServerStatusView$0methodref$getName$Type(){\n  $clinit_ServerStatusView$0methodref$getName$Type();\n}\n\ndefineClass(1728, 1, {1:1}, ServerStatusView$0methodref$getName$Type);\n_.apply_2 = function apply_193(arg0){\n  return castTo(arg0, 50).getName();\n}\n;\nvar Lorg_jboss_hal_client_runtime_server_ServerStatusView$0methodref$getName$Type_2_classLit = createForClass('org.jboss.hal.client.runtime.server', 'ServerStatusView/0methodref$getName$Type', 1728, Ljava_lang_Object_2_classLit);\nfunction $clinit_ServerStatusView$PreListItem(){\n  $clinit_ServerStatusView$PreListItem = emptyMethod;\n  $clinit_ListItem();\n}\n\nfunction ServerStatusView$PreListItem(name_0){\n  $clinit_ServerStatusView$PreListItem();\n  ListItem.call(this, name_0, (new LabelBuilder).label_3(name_0));\n  this.$init_1326();\n}\n\ndefineClass(1721, 428, {1:1, 10:1, 65:1, 89:1}, ServerStatusView$PreListItem);\n_.$init_1326 = function $init_1326(){\n}\n;\n_.setReadonlyValue = function setReadonlyValue_2(value_0){\n  this.setReadonlyValue_1(castTo(value_0, 13));\n}\n;\n_.assembleUI = function assembleUI_5(context){\n  getClassPrototype(428).assembleUI.call(this, context);\n  setVisible(this.valueElement, false);\n  this.pre = $createPreElement(getDocument());\n  $add($getClassList(this.pre), 'form-control-static');\n  $add($getClassList(this.pre), 'margin-bottom-5');\n  $appendChild_0(this.valueContainer, this.pre);\n}\n;\n_.setReadonlyValue_1 = function setReadonlyValue_3(value_0){\n  getClassPrototype(65).setReadonlyValue.call(this, value_0);\n  if (isNotNull(value_0)) {\n    $setTextContent(this.pre, join_4('\\n', value_0));\n  }\n}\n;\n_.setText_0 = function setText_10(text_0){\n  getClassPrototype(65).setText_0.call(this, text_0);\n  $setTextContent(this.pre, text_0);\n}\n;\nvar Lorg_jboss_hal_client_runtime_server_ServerStatusView$PreListItem_2_classLit = createForClass('org.jboss.hal.client.runtime.server', 'ServerStatusView/PreListItem', 1721, Lorg_jboss_hal_ballroom_form_ListItem_2_classLit);\nfunction $clinit_ServerStatusView$PreTextItem(){\n  $clinit_ServerStatusView$PreTextItem = emptyMethod;\n  $clinit_TextBoxItem();\n}\n\nfunction ServerStatusView$PreTextItem(name_0){\n  $clinit_ServerStatusView$PreTextItem();\n  TextBoxItem.call(this, name_0, (new LabelBuilder).label_3(name_0));\n  this.$init_1327();\n}\n\ndefineClass(581, 257, {1:1, 10:1, 65:1, 89:1}, ServerStatusView$PreTextItem);\n_.$init_1327 = function $init_1327(){\n}\n;\n_.setReadonlyValue = function setReadonlyValue_4(value_0){\n  this.setReadonlyValue_2(castToString(value_0));\n}\n;\n_.assembleUI = function assembleUI_6(context){\n  getClassPrototype(65).assembleUI.call(this, context);\n  setVisible(this.valueElement, false);\n  this.pre = $createPreElement(getDocument());\n  $add($getClassList(this.pre), 'form-control-static');\n  $add($getClassList(this.pre), 'margin-bottom-5');\n  $appendChild_0(this.valueContainer, this.pre);\n}\n;\n_.setReadonlyValue_2 = function setReadonlyValue_5(value_0){\n  getClassPrototype(65).setReadonlyValue.call(this, value_0);\n  $setTextContent(this.pre, value_0);\n}\n;\n_.setText_0 = function setText_11(text_0){\n  getClassPrototype(65).setText_0.call(this, text_0);\n  $setTextContent(this.pre, text_0);\n}\n;\nvar Lorg_jboss_hal_client_runtime_server_ServerStatusView$PreTextItem_2_classLit = createForClass('org.jboss.hal.client.runtime.server', 'ServerStatusView/PreTextItem', 581, Lorg_jboss_hal_ballroom_form_TextBoxItem_2_classLit);\nfunction $clinit_ServerStatusView$lambda$0$Type(){\n  $clinit_ServerStatusView$lambda$0$Type = emptyMethod;\n}\n\nfunction ServerStatusView$lambda$0$Type(){\n  $clinit_ServerStatusView$lambda$0$Type();\n}\n\ndefineClass(1722, 1, {1:1, 146:1}, ServerStatusView$lambda$0$Type);\n_.createFrom = function createFrom_15(arg0){\n  return lambda$0_219(arg0);\n}\n;\nvar Lorg_jboss_hal_client_runtime_server_ServerStatusView$lambda$0$Type_2_classLit = createForClass('org.jboss.hal.client.runtime.server', 'ServerStatusView/lambda$0$Type', 1722, Ljava_lang_Object_2_classLit);\nfunction $clinit_ServerStatusView$lambda$1$Type(){\n  $clinit_ServerStatusView$lambda$1$Type = emptyMethod;\n}\n\nfunction ServerStatusView$lambda$1$Type(){\n  $clinit_ServerStatusView$lambda$1$Type();\n}\n\ndefineClass(1723, 1, {1:1, 146:1}, ServerStatusView$lambda$1$Type);\n_.createFrom = function createFrom_16(arg0){\n  return lambda$1_132(arg0);\n}\n;\nvar Lorg_jboss_hal_client_runtime_server_ServerStatusView$lambda$1$Type_2_classLit = createForClass('org.jboss.hal.client.runtime.server', 'ServerStatusView/lambda$1$Type', 1723, Ljava_lang_Object_2_classLit);\nfunction $clinit_ServerStatusView$lambda$2$Type(){\n  $clinit_ServerStatusView$lambda$2$Type = emptyMethod;\n}\n\nfunction ServerStatusView$lambda$2$Type(){\n  $clinit_ServerStatusView$lambda$2$Type();\n}\n\ndefineClass(1724, 1, {1:1, 146:1}, ServerStatusView$lambda$2$Type);\n_.createFrom = function createFrom_17(arg0){\n  return lambda$2_105(arg0);\n}\n;\nvar Lorg_jboss_hal_client_runtime_server_ServerStatusView$lambda$2$Type_2_classLit = createForClass('org.jboss.hal.client.runtime.server', 'ServerStatusView/lambda$2$Type', 1724, Ljava_lang_Object_2_classLit);\nfunction $clinit_ServerStatusView$lambda$3$Type(){\n  $clinit_ServerStatusView$lambda$3$Type = emptyMethod;\n}\n\nfunction ServerStatusView$lambda$3$Type(){\n  $clinit_ServerStatusView$lambda$3$Type();\n}\n\ndefineClass(1725, 1, {1:1, 146:1}, ServerStatusView$lambda$3$Type);\n_.createFrom = function createFrom_18(arg0){\n  return lambda$3_81(arg0);\n}\n;\nvar Lorg_jboss_hal_client_runtime_server_ServerStatusView$lambda$3$Type_2_classLit = createForClass('org.jboss.hal.client.runtime.server', 'ServerStatusView/lambda$3$Type', 1725, Ljava_lang_Object_2_classLit);\nfunction $clinit_ServerStatusView$lambda$4$Type(){\n  $clinit_ServerStatusView$lambda$4$Type = emptyMethod;\n}\n\nfunction ServerStatusView$lambda$4$Type(){\n  $clinit_ServerStatusView$lambda$4$Type();\n}\n\ndefineClass(4427, $wnd.Function, {1:1}, ServerStatusView$lambda$4$Type);\n_.render_2 = function render_86(arg0, arg1, arg2, arg3){\n  return lambda$4_71(arg0, arg1, arg2, arg3);\n}\n;\nfunction $clinit_ServerStatusView$lambda$5$Type(){\n  $clinit_ServerStatusView$lambda$5$Type = emptyMethod;\n}\n\nfunction ServerStatusView$lambda$5$Type(){\n  $clinit_ServerStatusView$lambda$5$Type();\n}\n\ndefineClass(4428, $wnd.Function, {1:1}, ServerStatusView$lambda$5$Type);\n_.render_2 = function render_87(arg0, arg1, arg2, arg3){\n  return lambda$5_55(arg0, arg1, arg2, arg3);\n}\n;\nfunction $clinit_ServerStatusView$lambda$6$Type(){\n  $clinit_ServerStatusView$lambda$6$Type = emptyMethod;\n}\n\nfunction ServerStatusView$lambda$6$Type(){\n  $clinit_ServerStatusView$lambda$6$Type();\n}\n\ndefineClass(1726, 1, {1:1, 26:1}, ServerStatusView$lambda$6$Type);\n_.and = function and_66(other){\n  return $and(this, other);\n}\n;\n_.negate_1 = function negate_73(){\n  return $negate(this);\n}\n;\n_.or_1 = function or_72(other){\n  return $or(this, other);\n}\n;\n_.test_0 = function test_65(arg0){\n  return lambda$6_43(arg0);\n}\n;\nvar Lorg_jboss_hal_client_runtime_server_ServerStatusView$lambda$6$Type_2_classLit = createForClass('org.jboss.hal.client.runtime.server', 'ServerStatusView/lambda$6$Type', 1726, Ljava_lang_Object_2_classLit);\nfunction $clinit_ServerStatusView$lambda$7$Type(){\n  $clinit_ServerStatusView$lambda$7$Type = emptyMethod;\n}\n\nfunction ServerStatusView$lambda$7$Type(){\n  $clinit_ServerStatusView$lambda$7$Type();\n}\n\ndefineClass(1727, 1, {1:1}, ServerStatusView$lambda$7$Type);\n_.apply_2 = function apply_194(arg0){\n  return lambda$7_38(arg0);\n}\n;\nvar Lorg_jboss_hal_client_runtime_server_ServerStatusView$lambda$7$Type_2_classLit = createForClass('org.jboss.hal.client.runtime.server', 'ServerStatusView/lambda$7$Type', 1727, Ljava_lang_Object_2_classLit);\ndefineClass(1188, 1, {1:1});\n_.get_Key$type$org$jboss$hal$client$runtime$server$ServerStatusPresenter$MyView$_annotation$$none$$ = function get_Key$type$org$jboss$hal$client$runtime$server$ServerStatusPresenter$MyView$_annotation$$none$$(){\n  var result;\n  result = this.get_Key$type$org$jboss$hal$client$runtime$server$ServerStatusView$_annotation$$none$$();\n  return result;\n}\n;\n_.get_Key$type$org$jboss$hal$client$runtime$server$ServerStatusPresenter$_annotation$$none$$ = function get_Key$type$org$jboss$hal$client$runtime$server$ServerStatusPresenter$_annotation$$none$$(){\n  var result;\n  if (isNull_0(this.singleton_Key$type$org$jboss$hal$client$runtime$server$ServerStatusPresenter$_annotation$$none$$)) {\n    result = this.org$jboss$hal$client$runtime$server$ServerStatusPresenter_org$jboss$hal$client$runtime$server$ServerStatusPresenter_methodInjection(this.injector.getFragment_com_google_web_bindery_event_shared().get_Key$type$com$google$web$bindery$event$shared$EventBus$_annotation$$none$$(), this.get_Key$type$org$jboss$hal$client$runtime$server$ServerStatusPresenter$MyView$_annotation$$none$$(), this.get_Key$type$org$jboss$hal$client$runtime$server$ServerStatusPresenter$MyProxy$_annotation$$none$$(), this.injector.getFragment_org_jboss_hal_core_finder().get_Key$type$org$jboss$hal$core$finder$Finder$_annotation$$none$$(), this.injector.getFragment_org_jboss_hal_core_finder().get_Key$type$org$jboss$hal$core$finder$FinderPathFactory$_annotation$$none$$(), this.injector.getFragment_org_jboss_hal_dmr_dispatch().get_Key$type$org$jboss$hal$dmr$dispatch$Dispatcher$_annotation$$none$$(), this.injector.getFragment_org_jboss_hal_meta().get_Key$type$org$jboss$hal$meta$StatementContext$_annotation$$none$$(), this.injector.getFragment_org_jboss_hal_resources().get_Key$type$org$jboss$hal$resources$Resources$_annotation$$none$$());\n    this.memberInject_Key$type$org$jboss$hal$client$runtime$server$ServerStatusPresenter$_annotation$$none$$(result);\n    this.singleton_Key$type$org$jboss$hal$client$runtime$server$ServerStatusPresenter$_annotation$$none$$ = result;\n  }\n  return this.singleton_Key$type$org$jboss$hal$client$runtime$server$ServerStatusPresenter$_annotation$$none$$;\n}\n;\n_.get_Key$type$org$jboss$hal$client$runtime$server$ServerStatusView$_annotation$$none$$ = function get_Key$type$org$jboss$hal$client$runtime$server$ServerStatusView$_annotation$$none$$(){\n  var result;\n  if (isNull_0(this.singleton_Key$type$org$jboss$hal$client$runtime$server$ServerStatusView$_annotation$$none$$)) {\n    result = this.org$jboss$hal$client$runtime$server$ServerStatusView_org$jboss$hal$client$runtime$server$ServerStatusView_methodInjection(this.injector.getFragment_org_jboss_hal_meta().get_Key$type$org$jboss$hal$meta$MetadataRegistry$_annotation$$none$$(), this.injector.getFragment_org_jboss_hal_resources().get_Key$type$org$jboss$hal$resources$Resources$_annotation$$none$$());\n    this.memberInject_Key$type$org$jboss$hal$client$runtime$server$ServerStatusView$_annotation$$none$$(result);\n    this.singleton_Key$type$org$jboss$hal$client$runtime$server$ServerStatusView$_annotation$$none$$ = result;\n  }\n  return this.singleton_Key$type$org$jboss$hal$client$runtime$server$ServerStatusView$_annotation$$none$$;\n}\n;\n_.memberInject_Key$type$org$jboss$hal$client$runtime$server$ServerStatusPresenter$_annotation$$none$$ = function memberInject_Key$type$org$jboss$hal$client$runtime$server$ServerStatusPresenter$_annotation$$none$$(injectee){\n  this.injector.getFragment_com_gwtplatform_mvp_client().com$gwtplatform$mvp$client$HandlerContainerImpl_automaticBind_methodInjection________________________________________(injectee, this.injector.getFragment_com_gwtplatform_mvp_client().get_Key$type$com$gwtplatform$mvp$client$AutobindDisable$_annotation$$none$$());\n}\n;\n_.memberInject_Key$type$org$jboss$hal$client$runtime$server$ServerStatusView$_annotation$$none$$ = function memberInject_Key$type$org$jboss$hal$client$runtime$server$ServerStatusView$_annotation$$none$$(injectee){\n}\n;\n_.org$jboss$hal$client$runtime$server$ServerStatusPresenter_org$jboss$hal$client$runtime$server$ServerStatusPresenter_methodInjection = function org$jboss$hal$client$runtime$server$ServerStatusPresenter_org$jboss$hal$client$runtime$server$ServerStatusPresenter_methodInjection(_0, _1, _2, _3, _4, _5, _6, _7){\n  return new ServerStatusPresenter(_0, _1, _2, _3, _4, _5, _6, _7);\n}\n;\n_.org$jboss$hal$client$runtime$server$ServerStatusView_org$jboss$hal$client$runtime$server$ServerStatusView_methodInjection = function org$jboss$hal$client$runtime$server$ServerStatusView_org$jboss$hal$client$runtime$server$ServerStatusView_methodInjection(_0, _1){\n  return new ServerStatusView(_0, _1);\n}\n;\ndefineClass(1192, 1, {57:1, 1:1});\n_.onSuccess_1 = function onSuccess_218(){\n  this.val$callback2.onSuccess_0(this.this$11.this$01.get_Key$type$org$jboss$hal$client$runtime$server$ServerStatusPresenter$_annotation$$none$$());\n}\n;\n$entry(onLoad)(51);\n\n//# sourceURL=hal-51.js\n")