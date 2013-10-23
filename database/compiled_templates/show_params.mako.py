# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1381752142.797845
_template_filename='templates/show_params.mako'
_template_uri='show_params.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = ['inputs_recursive_indent', 'inputs_recursive']


def _mako_get_namespace(context, name):
    try:
        return context.namespaces[(__name__, name)]
    except KeyError:
        _mako_generate_namespaces(context)
        return context.namespaces[(__name__, name)]
def _mako_generate_namespaces(context):
    # SOURCE LINE 2
    ns = runtime.TemplateNamespace('__anon_0xdcda94c', context._clean_inheritance_tokens(), templateuri=u'/message.mako', callables=None, calling_uri=_template_uri)
    context.namespaces[(__name__, '__anon_0xdcda94c')] = ns

def _mako_inherit(template, context):
    _mako_generate_namespaces(context)
    return runtime._inherit_from(context, u'/base.mako', _template_uri)
def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0xdcda94c')._populate(_import_ns, [u'render_msg'])
        upgrade_messages = _import_ns.get('upgrade_messages', context.get('upgrade_messages', UNDEFINED))
        render_msg = _import_ns.get('render_msg', context.get('render_msg', UNDEFINED))
        h = _import_ns.get('h', context.get('h', UNDEFINED))
        tool = _import_ns.get('tool', context.get('tool', UNDEFINED))
        def inputs_recursive(input_params,param_values,depth=1,upgrade_messages=None):
            return render_inputs_recursive(context.locals_(__M_locals),input_params,param_values,depth,upgrade_messages)
        job = _import_ns.get('job', context.get('job', UNDEFINED))
        hda = _import_ns.get('hda', context.get('hda', UNDEFINED))
        inherit_chain = _import_ns.get('inherit_chain', context.get('inherit_chain', UNDEFINED))
        trans = _import_ns.get('trans', context.get('trans', UNDEFINED))
        params_objects = _import_ns.get('params_objects', context.get('params_objects', UNDEFINED))
        has_parameter_errors = _import_ns.get('has_parameter_errors', context.get('has_parameter_errors', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 1
        __M_writer(u'\n')
        # SOURCE LINE 2
        __M_writer(u'\n')
        # SOURCE LINE 3
        from galaxy.util import nice_size 
        
        __M_locals_builtin_stored = __M_locals_builtin()
        __M_locals.update(__M_dict_builtin([(__M_key, __M_locals_builtin_stored[__M_key]) for __M_key in ['nice_size'] if __M_key in __M_locals_builtin_stored]))
        __M_writer(u'\n\n<style>\n    .inherit {\n        border: 1px solid #bbb;\n        padding: 15px;\n        text-align: center;\n        background-color: #eee;\n    }\n</style>\n\n')
        # SOURCE LINE 88
        __M_writer(u'\n\n')
        # SOURCE LINE 95
        __M_writer(u'\n\n<table class="tabletip">\n    <thead>\n        <tr><th colspan="2" style="font-size: 120%;">\n')
        # SOURCE LINE 100
        if tool:
            # SOURCE LINE 101
            __M_writer(u'                Tool: ')
            __M_writer(filters.html_escape(unicode(tool.name )))
            __M_writer(u'\n')
            # SOURCE LINE 102
        else:
            # SOURCE LINE 103
            __M_writer(u'                Unknown Tool\n')
            pass
        # SOURCE LINE 105
        __M_writer(u'        </th></tr>\n    </thead>\n    <tbody>\n        ')
        # SOURCE LINE 108

        encoded_hda_id = trans.security.encode_id( hda.id )
        
        
        __M_locals_builtin_stored = __M_locals_builtin()
        __M_locals.update(__M_dict_builtin([(__M_key, __M_locals_builtin_stored[__M_key]) for __M_key in ['encoded_hda_id'] if __M_key in __M_locals_builtin_stored]))
        # SOURCE LINE 110
        __M_writer(u'\n        <tr><td>Name:</td><td>')
        # SOURCE LINE 111
        __M_writer(filters.html_escape(unicode(hda.name )))
        __M_writer(u'</td></tr>\n        <tr><td>Created:</td><td>')
        # SOURCE LINE 112
        __M_writer(unicode(hda.create_time.strftime("%b %d, %Y")))
        __M_writer(u'</td></tr>\n')
        # SOURCE LINE 114
        __M_writer(u'        <tr><td>Filesize:</td><td>')
        __M_writer(unicode(nice_size(hda.dataset.file_size)))
        __M_writer(u'</td></tr>\n        <tr><td>Dbkey:</td><td>')
        # SOURCE LINE 115
        __M_writer(filters.html_escape(unicode(hda.dbkey )))
        __M_writer(u'</td></tr>\n        <tr><td>Format:</td><td>')
        # SOURCE LINE 116
        __M_writer(filters.html_escape(unicode(hda.ext )))
        __M_writer(u'</td></tr>\n        <tr><td>Galaxy Tool Version:</td><td>')
        # SOURCE LINE 117
        __M_writer(filters.html_escape(unicode(job.tool_version )))
        __M_writer(u'</td></tr>\n        <tr><td>Tool Version:</td><td>')
        # SOURCE LINE 118
        __M_writer(filters.html_escape(unicode(hda.tool_version )))
        __M_writer(u'</td></tr>\n        <tr><td>Tool Standard Output:</td><td><a href="')
        # SOURCE LINE 119
        __M_writer(unicode(h.url_for( controller='dataset', action='stdout', dataset_id=encoded_hda_id )))
        __M_writer(u'">stdout</a></td></tr>\n        <tr><td>Tool Standard Error:</td><td><a href="')
        # SOURCE LINE 120
        __M_writer(unicode(h.url_for( controller='dataset', action='stderr', dataset_id=encoded_hda_id )))
        __M_writer(u'">stderr</a></td></tr>\n        <tr><td>Tool Exit Code:</td><td>')
        # SOURCE LINE 121
        __M_writer(filters.html_escape(unicode(job.exit_code )))
        __M_writer(u'</td></tr>\n        <tr><td>API ID:</td><td>')
        # SOURCE LINE 122
        __M_writer(unicode(encoded_hda_id))
        __M_writer(u'</td></tr>\n')
        # SOURCE LINE 123
        if trans.user_is_admin() or trans.app.config.expose_dataset_path:
            # SOURCE LINE 124
            __M_writer(u'            <tr><td>Full Path:</td><td>')
            __M_writer(filters.html_escape(unicode(hda.file_name )))
            __M_writer(u'</td></tr>\n')
            pass
        # SOURCE LINE 126
        __M_writer(u'</table>\n<br />\n\n<table class="tabletip">\n    <thead>\n        <tr>\n            <th>Input Parameter</th>\n            <th>Value</th>\n            <th>Note for rerun</th>\n        </tr>\n    </thead>\n    <tbody>\n')
        # SOURCE LINE 138
        if params_objects and tool:
            # SOURCE LINE 139
            __M_writer(u'            ')
            __M_writer(unicode( inputs_recursive( tool.inputs, params_objects, depth=1, upgrade_messages=upgrade_messages ) ))
            __M_writer(u'\n')
            # SOURCE LINE 140
        elif params_objects is None:
            # SOURCE LINE 141
            __M_writer(u'            <tr><td colspan="2">Unable to load parameters.</td></tr>\n')
            # SOURCE LINE 142
        else:
            # SOURCE LINE 143
            __M_writer(u'            <tr><td colspan="2">No parameters.</td></tr>\n')
            pass
        # SOURCE LINE 145
        __M_writer(u'    </tbody>\n</table>\n')
        # SOURCE LINE 147
        if has_parameter_errors:
            # SOURCE LINE 148
            __M_writer(u'    <br />\n    ')
            # SOURCE LINE 149
            __M_writer(unicode( render_msg( 'One or more of your original parameters may no longer be valid or displayed properly.', status='warning' ) ))
            __M_writer(u'\n')
            pass
        # SOURCE LINE 151
        __M_writer(u'\n    <h3>Inheritance Chain</h3>\n    <div class="inherit" style="background-color: #fff; font-weight:bold;">')
        # SOURCE LINE 153
        __M_writer(filters.html_escape(unicode(hda.name )))
        __M_writer(u'</div>\n\n')
        # SOURCE LINE 155
        for dep in inherit_chain:
            # SOURCE LINE 156
            __M_writer(u'        <div style="font-size: 36px; text-align: center; position: relative; top: 3px">&uarr;</div>\n        <div class="inherit">\n            \'')
            # SOURCE LINE 158
            __M_writer(filters.html_escape(unicode(dep[0].name )))
            __M_writer(u"' in ")
            __M_writer(unicode(dep[1]))
            __M_writer(u'<br/>\n        </div>\n')
            pass
        # SOURCE LINE 161
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_inputs_recursive_indent(context,text,depth):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0xdcda94c')._populate(_import_ns, [u'render_msg'])
        __M_writer = context.writer()
        # SOURCE LINE 91
        __M_writer(u'\n    <td style="padding-left: ')
        # SOURCE LINE 92
        __M_writer(unicode( ( depth - 1 ) * 10 ))
        __M_writer(u'px">\n        ')
        # SOURCE LINE 93
        __M_writer(filters.html_escape(unicode(text )))
        __M_writer(u'\n    </td> \n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_inputs_recursive(context,input_params,param_values,depth=1,upgrade_messages=None):
    context.caller_stack._push_frame()
    try:
        _import_ns = {}
        _mako_get_namespace(context, '__anon_0xdcda94c')._populate(_import_ns, [u'render_msg'])
        len = _import_ns.get('len', context.get('len', UNDEFINED))
        def inputs_recursive_indent(text,depth):
            return render_inputs_recursive_indent(context,text,depth)
        range = _import_ns.get('range', context.get('range', UNDEFINED))
        def inputs_recursive(input_params,param_values,depth=1,upgrade_messages=None):
            return render_inputs_recursive(context,input_params,param_values,depth,upgrade_messages)
        enumerate = _import_ns.get('enumerate', context.get('enumerate', UNDEFINED))
        trans = _import_ns.get('trans', context.get('trans', UNDEFINED))
        hasattr = _import_ns.get('hasattr', context.get('hasattr', UNDEFINED))
        __M_writer = context.writer()
        # SOURCE LINE 14
        __M_writer(u'\n    ')
        # SOURCE LINE 15

        if upgrade_messages is None:
            upgrade_messages = {}
            
        
        # SOURCE LINE 18
        __M_writer(u'\n')
        # SOURCE LINE 19
        for input_index, input in enumerate( input_params.itervalues() ):
            # SOURCE LINE 20
            if input.name in param_values:
                # SOURCE LINE 21
                if input.type == "repeat":
                    # SOURCE LINE 22
                    for i in range( len(param_values[input.name]) ):
                        # SOURCE LINE 23
                        __M_writer(u'                    ')
                        __M_writer(unicode( inputs_recursive(input.inputs, param_values[input.name][i], depth=depth+1) ))
                        __M_writer(u'\n')
                        pass
                    # SOURCE LINE 25
                elif input.type == "conditional":
                    # SOURCE LINE 26
                    __M_writer(u'                ')
 
                    try:
                        current_case = param_values[input.name]['__current_case__']
                        is_valid = True
                    except:
                        current_case = None
                        is_valid = False
                    
                    
                    # SOURCE LINE 33
                    __M_writer(u'\n')
                    # SOURCE LINE 34
                    if is_valid:
                        # SOURCE LINE 35
                        __M_writer(u'                    <tr>\n                        ')
                        # SOURCE LINE 36
                        __M_writer(unicode( inputs_recursive_indent( text=input.test_param.label, depth=depth )))
                        __M_writer(u'\n')
                        # SOURCE LINE 38
                        __M_writer(u'                        <td>')
                        __M_writer(filters.html_escape(unicode(input.cases[current_case].value )))
                        __M_writer(u'</td>\n                        <td></td>\n                    </tr>\n                    ')
                        # SOURCE LINE 41
                        __M_writer(unicode( inputs_recursive( input.cases[current_case].inputs, param_values[input.name], depth=depth+1, upgrade_messages=upgrade_messages.get( input.name ) ) ))
                        __M_writer(u'\n')
                        # SOURCE LINE 42
                    else:
                        # SOURCE LINE 43
                        __M_writer(u'                    <tr>\n                        ')
                        # SOURCE LINE 44
                        __M_writer(unicode( inputs_recursive_indent( text=input.name, depth=depth )))
                        __M_writer(u'\n                        <td><em>The previously used value is no longer valid</em></td>\n                        <td></td>\n                    </tr>\n')
                        pass
                    # SOURCE LINE 49
                elif input.type == "upload_dataset":
                    # SOURCE LINE 50
                    __M_writer(u'                    <tr>\n                        ')
                    # SOURCE LINE 51
                    __M_writer(unicode(inputs_recursive_indent( text=input.group_title( param_values ), depth=depth )))
                    __M_writer(u'\n                        <td>')
                    # SOURCE LINE 52
                    __M_writer(unicode( len( param_values[input.name] ) ))
                    __M_writer(u' uploaded datasets</td>\n                        <td></td>\n                    </tr>\n')
                    # SOURCE LINE 55
                elif input.visible:
                    # SOURCE LINE 56
                    __M_writer(u'                ')
 
                    if  hasattr( input, "label" ) and input.label:
                        label = input.label
                    else:
                        #value for label not required, fallback to input name (same as tool panel)
                        label = input.name
                    
                    
                    # SOURCE LINE 62
                    __M_writer(u'\n                <tr>\n                    ')
                    # SOURCE LINE 64
                    __M_writer(unicode(inputs_recursive_indent( text=label, depth=depth )))
                    __M_writer(u'\n                    <td>')
                    # SOURCE LINE 65
                    __M_writer(filters.html_escape(unicode(input.value_to_display_text( param_values[input.name], trans.app ) )))
                    __M_writer(u'</td>\n                    <td>')
                    # SOURCE LINE 66
                    __M_writer(filters.html_escape(unicode( upgrade_messages.get( input.name, '' ) )))
                    __M_writer(u'</td>\n                </tr>\n')
                    pass
                # SOURCE LINE 69
            else:
                # SOURCE LINE 71
                __M_writer(u'            <tr>\n                ')
                # SOURCE LINE 72

                    # Get parameter label.  
                if input.type == "conditional":
                    label = input.test_param.label
                elif input.type == "repeat":
                    label = input.label()
                else:
                    label = input.label or input.name
                                
                
                # SOURCE LINE 80
                __M_writer(u'\n                ')
                # SOURCE LINE 81
                __M_writer(unicode(inputs_recursive_indent( text=label, depth=depth )))
                __M_writer(u'\n                <td><em>not used (parameter was added after this job was run)</em></td>\n                <td></td>\n            </tr>\n')
                pass
            # SOURCE LINE 86
            __M_writer(u'        \n')
            pass
        return ''
    finally:
        context.caller_stack._pop_frame()


