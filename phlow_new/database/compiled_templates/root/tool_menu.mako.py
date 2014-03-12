# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1392805205.65713
_template_filename=u'templates/webapps/galaxy/root/tool_menu.mako'
_template_uri=u'/root/tool_menu.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = ['tool_menu_javascripts', 'render_tool_menu']


def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        __M_writer = context.writer()
        # SOURCE LINE 58
        __M_writer(u'\n\n')
        # SOURCE LINE 98
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_tool_menu_javascripts(context):
    context.caller_stack._push_frame()
    try:
        h = context.get('h', UNDEFINED)
        trans = context.get('trans', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 2
        __M_writer(u'\n    ')
        # SOURCE LINE 3
        __M_writer(unicode(h.templates( "tool_link", "panel_section", "tool_search" )))
        __M_writer(u'\n    ')
        # SOURCE LINE 4
        __M_writer(unicode(h.js( "libs/require", "galaxy.autocom_tagging" )))
        __M_writer(u'\n    \n    <script type="text/javascript">\n\n        require.config({ \n                baseUrl: "')
        # SOURCE LINE 9
        __M_writer(unicode(h.url_for('/static/scripts')))
        __M_writer(u'",\n                shim: {\n                    "libs/underscore": { exports: "_" }\n                }\n        });\n\n        require(["mvc/tools"], function(tools_mod) {\n\n            // Init. on document load.\n            $(function() {\n')
        # SOURCE LINE 20
        if trans.user or not trans.app.config.require_login:
            # SOURCE LINE 21
            __M_writer(u'                    // Create tool search, tool panel, and tool panel view.\n                    var tool_search = new tools_mod.ToolSearch({ \n                            spinner_url: "')
            # SOURCE LINE 23
            __M_writer(unicode(h.url_for('/static/images/loading_small_white_bg.gif')))
            __M_writer(u'",\n                            search_url: "')
            # SOURCE LINE 24
            __M_writer(unicode(h.url_for( controller='root', action='tool_search' )))
            __M_writer(u'",\n                            hidden: false \n                        }),\n                        tools = new tools_mod.ToolCollection( \n                                    ')
            # SOURCE LINE 28
            __M_writer(unicode( h.to_json_string( trans.app.toolbox.to_dict( trans, in_panel=False ) ) ))
            __M_writer(u' \n                                                        ),\n                        tool_panel = new tools_mod.ToolPanel({ \n                            tool_search: tool_search,\n                            tools: tools,\n                            layout: ')
            # SOURCE LINE 33
            __M_writer(unicode(h.to_json_string( trans.app.toolbox.to_dict( trans ) )))
            __M_writer(u'\n                        }),\n                        tool_panel_view = new tools_mod.ToolPanelView({ model: tool_panel });\n                    \n                    // Add tool panel to Galaxy object.\n                    Galaxy.toolPanel = tool_panel;\n\n                    // If there are tools, render panel and display everything.\n                    if (tool_panel.get(\'layout\').size() > 1) {\n                        tool_panel_view.render();\n                        $(\'.toolMenu\').show();\n                    }\n                    $(\'.toolMenuContainer\').prepend(tool_panel_view.$el);\n                    \n                    // Minsize init hint.\n                    $( "a[minsizehint]" ).click( function() {\n                        if ( parent.handle_minwidth_hint ) {\n                            parent.handle_minwidth_hint( $(this).attr( "minsizehint" ) );\n                        }\n                    });\n')
            pass
        # SOURCE LINE 54
        __M_writer(u'            });\n\n        });\n    </script>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_render_tool_menu(context):
    context.caller_stack._push_frame()
    try:
        util = context.get('util', UNDEFINED)
        h = context.get('h', UNDEFINED)
        trans = context.get('trans', UNDEFINED)
        t = context.get('t', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 61
        __M_writer(u'\n    <div class="toolMenuContainer">\n        \n        <div class="toolMenu" style="display: none">\n')
        # SOURCE LINE 66
        __M_writer(u'            <div id="search-no-results" style="display: none; padding-top: 5px">\n                <em><strong>Search did not match any tools.</strong></em>\n            </div>\n            \n')
        # SOURCE LINE 73
        __M_writer(u'            \n')
        # SOURCE LINE 74
        if t.user:
            # SOURCE LINE 75
            __M_writer(u'                <div class="toolSectionPad"></div>\n                <div class="toolSectionPad"></div>\n                <div class="toolSectionTitle" id="title_XXinternalXXworkflow">\n                  <span>Workflows</span>\n                </div>\n                <div id="XXinternalXXworkflow" class="toolSectionBody">\n                    <div class="toolSectionBg">\n')
            # SOURCE LINE 82
            if t.user.stored_workflow_menu_entries:
                # SOURCE LINE 83
                for m in t.user.stored_workflow_menu_entries:
                    # SOURCE LINE 84
                    __M_writer(u'                                <div class="toolTitle">\n                                    <a href="')
                    # SOURCE LINE 85
                    __M_writer(unicode(h.url_for( controller='workflow', action='run', id=trans.security.encode_id(m.stored_workflow_id) )))
                    __M_writer(u'" target="galaxy_main">')
                    __M_writer(unicode( util.unicodify( m.stored_workflow.name ) ))
                    __M_writer(u'</a>\n                                </div>\n')
                    pass
                pass
            # SOURCE LINE 89
            __M_writer(u'                        <div class="toolTitle">\n                            <a href="')
            # SOURCE LINE 90
            __M_writer(unicode(h.url_for( controller='workflow', action='list_for_run')))
            __M_writer(u'" target="galaxy_main">All workflows</a>\n                        </div>\n                    </div>\n                </div>\n')
            pass
        # SOURCE LINE 95
        __M_writer(u'            \n        </div>\n    </div>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


