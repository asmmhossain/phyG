# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1383500900.092349
_template_filename='templates/webapps/galaxy/dataset/edit_attributes.mako'
_template_uri='/dataset/edit_attributes.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = ['stylesheets', 'datatype', 'javascripts', 'title']


def _mako_get_namespace(context, name):
    try:
        return context.namespaces[(__name__, name)]
    except KeyError:
        _mako_generate_namespaces(context)
        return context.namespaces[(__name__, name)]
def _mako_generate_namespaces(context):
    # SOURCE LINE 3
    ns = runtime.TemplateNamespace('__anon_0xaa67d64cL', context._clean_inheritance_tokens(), templateuri=u'/refresh_frames.mako', callables=None, calling_uri=_template_uri)
    context.namespaces[(__name__, '__anon_0xaa67d64cL')] = ns

    # SOURCE LINE 2
    ns = runtime.TemplateNamespace('__anon_0xaa67d80cL', context._clean_inheritance_tokens(), templateuri=u'/message.mako', callables=None, calling_uri=_template_uri)
    context.namespaces[(__name__, '__anon_0xaa67d80cL')] = ns

    # SOURCE LINE 175
    ns = runtime.TemplateNamespace('__anon_0xa9fcaf4cL', context._clean_inheritance_tokens(), templateuri=u'/dataset/security_common.mako', callables=None, calling_uri=_template_uri)
    context.namespaces[(__name__, '__anon_0xa9fcaf4cL')] = ns

def _mako_inherit(template, context):
    _mako_generate_namespaces(context)
    return runtime._inherit_from(context, u'/base.mako', _template_uri)
def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0xaa67d64cL')._populate(_import_ns, [u'handle_refresh_frames'])
        _mako_get_namespace(context, '__anon_0xaa67d80cL')._populate(_import_ns, [u'render_msg'])
        _mako_get_namespace(context, '__anon_0xa9fcaf4cL')._populate(_import_ns, [u'render_permission_form'])
        status = _import_ns.get('status', context.get('status', UNDEFINED))
        render_permission_form = _import_ns.get('render_permission_form', context.get('render_permission_form', UNDEFINED))
        render_msg = _import_ns.get('render_msg', context.get('render_msg', UNDEFINED))
        all_roles = _import_ns.get('all_roles', context.get('all_roles', UNDEFINED))
        data_annotation = _import_ns.get('data_annotation', context.get('data_annotation', UNDEFINED))
        def datatype(dataset,datatypes):
            return render_datatype(context.locals_(__M_locals),dataset,datatypes)
        h = _import_ns.get('h', context.get('h', UNDEFINED))
        current_user_roles = _import_ns.get('current_user_roles', context.get('current_user_roles', UNDEFINED))
        len = _import_ns.get('len', context.get('len', UNDEFINED))
        util = _import_ns.get('util', context.get('util', UNDEFINED))
        datatypes = _import_ns.get('datatypes', context.get('datatypes', UNDEFINED))
        message = _import_ns.get('message', context.get('message', UNDEFINED))
        trans = _import_ns.get('trans', context.get('trans', UNDEFINED))
        data = _import_ns.get('data', context.get('data', UNDEFINED))
        dataset_id = _import_ns.get('dataset_id', context.get('dataset_id', UNDEFINED))
        _ = _import_ns.get('_', context.get('_', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 1
        __M_writer(u'\n')
        # SOURCE LINE 2
        __M_writer(u'\n')
        # SOURCE LINE 3
        __M_writer(u'\n\n')
        # SOURCE LINE 5
        __M_writer(u'\n\n')
        # SOURCE LINE 9
        __M_writer(u'\n\n')
        # SOURCE LINE 15
        __M_writer(u'\n\n')
        # SOURCE LINE 27
        __M_writer(u'\n\n')
        # SOURCE LINE 29
        if message:
            # SOURCE LINE 30
            __M_writer(u'    ')
            __M_writer(unicode(render_msg( message, status )))
            __M_writer(u'\n')
            pass
        # SOURCE LINE 32
        __M_writer(u'\n\n<ul class="nav nav-tabs">\n    <li class="active"><a href="#attributes" data-toggle="tab">Attributes</a></li>\n    <li><a href="#convert" data-toggle="tab">Convert Format</a></li>\n    <li><a href="#datatype" data-toggle="tab">Datatype</a></li>\n    <li><a href="#permissions" data-toggle="tab">Permissions</a></li>\n</ul>\n\n<div class="tab-content">\n\n<div class="tab-pane active toolForm" id="attributes">\n    <div class="toolFormTitle">')
        # SOURCE LINE 44
        __M_writer(unicode(_('Edit Attributes')))
        __M_writer(u'</div>\n    <div class="toolFormBody">\n        <form name="edit_attributes" action="')
        # SOURCE LINE 46
        __M_writer(unicode(h.url_for( controller='dataset', action='edit', dataset_id=dataset_id )))
        __M_writer(u'" method="post">\n            <div class="form-row">\n                <label>\n                    Name:\n                </label>\n                <div style="float: left; width: 250px; margin-right: 10px;">\n                    <input type="text" name="name" value="')
        # SOURCE LINE 52
        __M_writer(filters.html_escape(unicode(data.get_display_name() )))
        __M_writer(u'" size="40"/>\n                </div>\n                <div style="clear: both"></div>\n            </div>\n            <div class="form-row">\n                <label>\n                    Info:\n                </label>\n                <div style="float: left; width: 250px; margin-right: 10px;">\n                    ')
        # SOURCE LINE 61
        info = data.info if data.info else '' 
        
        __M_locals_builtin_stored = __M_locals_builtin()
        __M_locals.update(__M_dict_builtin([(__M_key, __M_locals_builtin_stored[__M_key]) for __M_key in ['info'] if __M_key in __M_locals_builtin_stored]))
        __M_writer(u'\n                    <textarea name="info" cols="40" rows="2">')
        # SOURCE LINE 62
        __M_writer(filters.html_escape(unicode( util.unicodify( info ) )))
        __M_writer(u'</textarea>\n                </div>\n                <div style="clear: both"></div>\n            </div>\n')
        # SOURCE LINE 66
        if trans.get_user() is not None:
            # SOURCE LINE 67
            __M_writer(u'                <div class="form-row">                    \n                    <label>\n                        Annotation / Notes:\n                    </label>\n                    <div style="float: left; width: 250px; margin-right: 10px;">\n                        ')
            # SOURCE LINE 72
            annotation = data_annotation if data_annotation else '' 
            
            __M_locals_builtin_stored = __M_locals_builtin()
            __M_locals.update(__M_dict_builtin([(__M_key, __M_locals_builtin_stored[__M_key]) for __M_key in ['annotation'] if __M_key in __M_locals_builtin_stored]))
            __M_writer(u'\n                        <textarea name="annotation" cols="40" rows="2">')
            # SOURCE LINE 73
            __M_writer(filters.html_escape(unicode(annotation )))
            __M_writer(u'</textarea>\n                    </div>\n                    <div style="clear: both"></div>\n                    <div class="toolParamHelp">Add an annotation or notes to a dataset; annotations are available when a history is viewed.</div>\n                </div>\n')
            pass
        # SOURCE LINE 79
        for name, spec in data.metadata.spec.items():
            # SOURCE LINE 80
            if spec.visible:
                # SOURCE LINE 81
                __M_writer(u'                    <div class="form-row">\n                        <label>\n                            ')
                # SOURCE LINE 83
                __M_writer(unicode(spec.desc))
                __M_writer(u':\n                        </label>\n                        <div style="float: left; width: 250px; margin-right: 10px;">\n                            ')
                # SOURCE LINE 86
                __M_writer(unicode(data.metadata.get_html_by_name( name, trans=trans )))
                __M_writer(u'\n                        </div>\n                        <div style="clear: both"></div>\n                    </div>\n')
                pass
            pass
        # SOURCE LINE 92
        __M_writer(u'            <div class="form-row">\n                <input type="submit" name="save" value="')
        # SOURCE LINE 93
        __M_writer(unicode(_('Save')))
        __M_writer(u'"/>\n            </div>\n        </form>\n        <form name="auto_detect" action="')
        # SOURCE LINE 96
        __M_writer(unicode(h.url_for( controller='dataset', action='edit', dataset_id=dataset_id )))
        __M_writer(u'" method="post">\n            <div class="form-row">\n                <div style="float: left; width: 250px; margin-right: 10px;">\n                    <input type="submit" name="detect" value="')
        # SOURCE LINE 99
        __M_writer(unicode(_('Auto-detect')))
        __M_writer(u'"/>\n                </div>\n                <div class="toolParamHelp" style="clear: both;">\n                    This will inspect the dataset and attempt to correct the above column values if they are not accurate.\n                </div>\n            </div>\n        </form>\n')
        # SOURCE LINE 106
        if data.missing_meta():
            # SOURCE LINE 107
            __M_writer(u'            <div class="form-row">\n                <div class="errormessagesmall">')
            # SOURCE LINE 108
            __M_writer(unicode(_('Required metadata values are missing. Some of these values may not be editable by the user. Selecting "Auto-detect" will attempt to fix these values.')))
            __M_writer(u'</div>\n            </div>\n')
            pass
        # SOURCE LINE 111
        __M_writer(u'    </div>\n</div>\n\n<div class="tab-pane toolForm" id="convert">\n    <div class="toolFormTitle">')
        # SOURCE LINE 115
        __M_writer(unicode(_('Convert to new format')))
        __M_writer(u'</div>\n    <div class="toolFormBody">\n        ')
        # SOURCE LINE 117
        converters = data.get_converter_types() 
        
        __M_locals_builtin_stored = __M_locals_builtin()
        __M_locals.update(__M_dict_builtin([(__M_key, __M_locals_builtin_stored[__M_key]) for __M_key in ['converters'] if __M_key in __M_locals_builtin_stored]))
        __M_writer(u'\n')
        # SOURCE LINE 118
        if len( converters ) > 0:
            # SOURCE LINE 119
            __M_writer(u'            <form name="convert_data" action="')
            __M_writer(unicode(h.url_for( controller='dataset', action='edit', dataset_id=dataset_id )))
            __M_writer(u'" method="post">\n                <div class="form-row">\n                    <div style="float: left; width: 250px; margin-right: 10px;">\n                        <select name="target_type">\n')
            # SOURCE LINE 123
            for key, value in converters.items():
                # SOURCE LINE 124
                __M_writer(u'                                <option value="')
                __M_writer(unicode(key))
                __M_writer(u'">')
                __M_writer(unicode(value.name))
                __M_writer(u'</option>\n')
                pass
            # SOURCE LINE 126
            __M_writer(u'                        </select>\n                    </div>\n                    <div class="toolParamHelp" style="clear: both;">\n                        This will create a new dataset with the contents of this dataset converted to a new format. \n                    </div>\n                    <div style="clear: both"></div>\n                </div>\n                <div class="form-row">\n                    <input type="submit" name="convert_data" value="')
            # SOURCE LINE 134
            __M_writer(unicode(_('Convert')))
            __M_writer(u'"/>\n                </div>\n            </form>\n')
            # SOURCE LINE 137
        else:
            # SOURCE LINE 138
            __M_writer(u'            No conversions available\n')
            pass
        # SOURCE LINE 140
        __M_writer(u'    </div>\n</div>\n\n<div class="tab-pane toolForm" id="datatype">\n    <div class="toolFormTitle">')
        # SOURCE LINE 144
        __M_writer(unicode(_('Change data type')))
        __M_writer(u'</div>\n    <div class="toolFormBody">\n')
        # SOURCE LINE 146
        if data.datatype.allow_datatype_change:
            # SOURCE LINE 147
            __M_writer(u'            <form name="change_datatype" action="')
            __M_writer(unicode(h.url_for( controller='dataset', action='edit', dataset_id=dataset_id )))
            __M_writer(u'" method="post">\n                <div class="form-row">\n                    <label>\n                        ')
            # SOURCE LINE 150
            __M_writer(unicode(_('New Type')))
            __M_writer(u':\n                    </label>\n                    <div style="float: left; width: 250px; margin-right: 10px;">\n                        ')
            # SOURCE LINE 153
            __M_writer(unicode(datatype( data, datatypes )))
            __M_writer(u'\n                    </div>\n                    <div class="toolParamHelp" style="clear: both;">\n                        ')
            # SOURCE LINE 156
            __M_writer(unicode(_('This will change the datatype of the existing dataset but <i>not</i> modify its contents. Use this if Galaxy has incorrectly guessed the type of your dataset.')))
            __M_writer(u'\n                    </div>\n                    <div style="clear: both"></div>\n                </div>\n                <div class="form-row">\n                    <input type="submit" name="change" value="')
            # SOURCE LINE 161
            __M_writer(unicode(_('Save')))
            __M_writer(u'"/>\n                </div>\n            </form>\n')
            # SOURCE LINE 164
        else:
            # SOURCE LINE 165
            __M_writer(u'            <div class="form-row">\n                <div class="warningmessagesmall">')
            # SOURCE LINE 166
            __M_writer(unicode(_('Changing the datatype of this dataset is not allowed.')))
            __M_writer(u'</div>\n            </div>\n')
            pass
        # SOURCE LINE 169
        __M_writer(u'    </div>\n</div>\n<p />\n\n<div class="tab-pane" id="permissions">\n')
        # SOURCE LINE 174
        if trans.app.security_agent.can_manage_dataset( current_user_roles, data.dataset ):
            # SOURCE LINE 175
            __M_writer(u'    ')
            __M_writer(u'\n    ')
            # SOURCE LINE 176
            __M_writer(unicode(render_permission_form( data.dataset, data.get_display_name(), h.url_for( controller='dataset', action='edit', dataset_id=dataset_id ), all_roles )))
            __M_writer(u'\n')
            # SOURCE LINE 177
        elif trans.user:
            # SOURCE LINE 178
            __M_writer(u'    <div class="toolForm">\n        <div class="toolFormTitle">View Permissions</div>\n        <div class="toolFormBody">\n            <div class="form-row">\n')
            # SOURCE LINE 182
            if data.dataset.actions:
                # SOURCE LINE 183
                __M_writer(u'                    <ul>\n')
                # SOURCE LINE 184
                for action, roles in trans.app.security_agent.get_permissions( data.dataset ).items():
                    # SOURCE LINE 185
                    if roles:
                        # SOURCE LINE 186
                        __M_writer(u'                                <li>')
                        __M_writer(unicode(action.description))
                        __M_writer(u'</li>\n                                <ul>\n')
                        # SOURCE LINE 188
                        for role in roles:
                            # SOURCE LINE 189
                            __M_writer(u'                                        <li>')
                            __M_writer(unicode(role.name))
                            __M_writer(u'</li>\n')
                            pass
                        # SOURCE LINE 191
                        __M_writer(u'                                </ul>\n')
                        pass
                    pass
                # SOURCE LINE 194
                __M_writer(u'                    </ul>\n')
                # SOURCE LINE 195
            else:
                # SOURCE LINE 196
                __M_writer(u'                    <p>This dataset is accessible by everyone (it is public).</p>\n')
                pass
            # SOURCE LINE 198
            __M_writer(u'            </div>\n        </div>\n    </div>\n')
            # SOURCE LINE 201
        else:
            # SOURCE LINE 202
            __M_writer(u'    Permissions not available (not logged in)\n')
            pass
        # SOURCE LINE 204
        __M_writer(u'</div>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_stylesheets(context):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0xaa67d64cL')._populate(_import_ns, [u'handle_refresh_frames'])
        _mako_get_namespace(context, '__anon_0xaa67d80cL')._populate(_import_ns, [u'render_msg'])
        _mako_get_namespace(context, '__anon_0xa9fcaf4cL')._populate(_import_ns, [u'render_permission_form'])
        h = _import_ns.get('h', context.get('h', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 7
        __M_writer(u'\n    ')
        # SOURCE LINE 8
        __M_writer(unicode(h.css( "base", "autocomplete_tagging" )))
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_datatype(context,dataset,datatypes):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0xaa67d64cL')._populate(_import_ns, [u'handle_refresh_frames'])
        _mako_get_namespace(context, '__anon_0xaa67d80cL')._populate(_import_ns, [u'render_msg'])
        _mako_get_namespace(context, '__anon_0xa9fcaf4cL')._populate(_import_ns, [u'render_permission_form'])
        _ = _import_ns.get('_', context.get('_', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 17
        __M_writer(u'\n    <select name="datatype">\n')
        # SOURCE LINE 19
        for ext in datatypes:
            # SOURCE LINE 20
            if dataset.ext == ext:
                # SOURCE LINE 21
                __M_writer(u'                <option value="')
                __M_writer(unicode(ext))
                __M_writer(u'" selected="yes">')
                __M_writer(unicode(_(ext)))
                __M_writer(u'</option>\n')
                # SOURCE LINE 22
            else:
                # SOURCE LINE 23
                __M_writer(u'                <option value="')
                __M_writer(unicode(ext))
                __M_writer(u'">')
                __M_writer(unicode(_(ext)))
                __M_writer(u'</option>\n')
                pass
            pass
        # SOURCE LINE 26
        __M_writer(u'    </select>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_javascripts(context):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0xaa67d64cL')._populate(_import_ns, [u'handle_refresh_frames'])
        _mako_get_namespace(context, '__anon_0xaa67d80cL')._populate(_import_ns, [u'render_msg'])
        _mako_get_namespace(context, '__anon_0xa9fcaf4cL')._populate(_import_ns, [u'render_permission_form'])
        h = _import_ns.get('h', context.get('h', UNDEFINED))
        handle_refresh_frames = _import_ns.get('handle_refresh_frames', context.get('handle_refresh_frames', UNDEFINED))
        parent = _import_ns.get('parent', context.get('parent', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 11
        __M_writer(u'\n    ')
        # SOURCE LINE 12
        __M_writer(unicode(parent.javascripts()))
        __M_writer(u'\n    ')
        # SOURCE LINE 13
        __M_writer(unicode(handle_refresh_frames()))
        __M_writer(u'\n    ')
        # SOURCE LINE 14
        __M_writer(unicode(h.js( "libs/jquery/jquery.autocomplete", "galaxy.autocom_tagging" )))
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_title(context):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0xaa67d64cL')._populate(_import_ns, [u'handle_refresh_frames'])
        _mako_get_namespace(context, '__anon_0xaa67d80cL')._populate(_import_ns, [u'render_msg'])
        _mako_get_namespace(context, '__anon_0xa9fcaf4cL')._populate(_import_ns, [u'render_permission_form'])
        _ = _import_ns.get('_', context.get('_', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 5
        __M_writer(unicode(_('Edit Dataset Attributes')))
        return ''
    finally:
        context.caller_stack._pop_frame()


