# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1381256487.58775
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
        # SOURCE LINE 53
        __M_writer(u'\n\n')
        # SOURCE LINE 93
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
        __M_writer(u'",\n                shim: {\n                    "libs/underscore": { exports: "_" }\n                }\n        });\n\n        require(["mvc/tools"], function(tools) {\n\n            // Init. on document load.\n            $(function() {\n                // Create tool search, tool panel, and tool panel view.\n                var tool_search = new tools.ToolSearch({ \n                        spinner_url: "')
        # SOURCE LINE 21
        __M_writer(unicode(h.url_for('/static/images/loading_small_white_bg.gif')))
        __M_writer(u'",\n                        search_url: "')
        # SOURCE LINE 22
        __M_writer(unicode(h.url_for( controller='root', action='tool_search' )))
        __M_writer(u'",\n                        hidden: false \n                    }),\n                    tool_panel = new tools.ToolPanel({ tool_search: tool_search }),\n                    tool_panel_view = new tools.ToolPanelView({ collection: tool_panel });\n\n                // Add tool panel to Galaxy object.\n                Galaxy.toolPanel = tool_panel;\n\n')
        # SOURCE LINE 32
        if trans.user or not trans.app.config.require_login:
            # SOURCE LINE 33
            __M_writer(u'                    tool_panel.reset( tool_panel.parse( ')
            __M_writer(unicode(h.to_json_string( trans.app.toolbox.to_dict( trans ) )))
            __M_writer(u' ) );\n')
            pass
        # SOURCE LINE 35
        __M_writer(u'\n                // If there are tools, render panel and display everything.\n                if (tool_panel.length > 1) { // > 1 because tool_search counts as a model\n                    tool_panel_view.render();\n                    $(\'.toolMenu\').show();\n                }\n                $(\'.toolMenuContainer\').prepend(tool_panel_view.$el);\n                \n                // Minsize init hint.\n                $( "a[minsizehint]" ).click( function() {\n                    if ( parent.handle_minwidth_hint ) {\n                        parent.handle_minwidth_hint( $(this).attr( "minsizehint" ) );\n                    }\n                });\n            });\n\n        });\n    </script>\n')
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
        # SOURCE LINE 56
        __M_writer(u'\n    <div class="toolMenuContainer">\n        \n        <div class="toolMenu" style="display: none">\n')
        # SOURCE LINE 61
        __M_writer(u'            <div id="search-no-results" style="display: none; padding-top: 5px">\n                <em><strong>Search did not match any tools.</strong></em>\n            </div>\n            \n')
        # SOURCE LINE 68
        __M_writer(u'            \n')
        # SOURCE LINE 69
        if t.user:
            # SOURCE LINE 70
            __M_writer(u'                <div class="toolSectionPad"></div>\n                <div class="toolSectionPad"></div>\n                <div class="toolSectionTitle" id="title_XXinternalXXworkflow">\n                  <span>Workflows</span>\n                </div>\n                <div id="XXinternalXXworkflow" class="toolSectionBody">\n                    <div class="toolSectionBg">\n')
            # SOURCE LINE 77
            if t.user.stored_workflow_menu_entries:
                # SOURCE LINE 78
                for m in t.user.stored_workflow_menu_entries:
                    # SOURCE LINE 79
                    __M_writer(u'                                <div class="toolTitle">\n                                    <a href="')
                    # SOURCE LINE 80
                    __M_writer(unicode(h.url_for( controller='workflow', action='run', id=trans.security.encode_id(m.stored_workflow_id) )))
                    __M_writer(u'" target="galaxy_main">')
                    __M_writer(unicode( util.unicodify( m.stored_workflow.name ) ))
                    __M_writer(u'</a>\n                                </div>\n')
                    pass
                pass
            # SOURCE LINE 84
            __M_writer(u'                        <div class="toolTitle">\n                            <a href="')
            # SOURCE LINE 85
            __M_writer(unicode(h.url_for( controller='workflow', action='list_for_run')))
            __M_writer(u'" target="galaxy_main">All workflows</a>\n                        </div>\n                    </div>\n                </div>\n')
            pass
        # SOURCE LINE 90
        __M_writer(u'            \n        </div>\n    </div>\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


