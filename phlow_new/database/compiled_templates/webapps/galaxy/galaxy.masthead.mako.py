# -*- encoding:ascii -*-
from mako import runtime, filters, cache
UNDEFINED = runtime.UNDEFINED
__M_dict_builtin = dict
__M_locals_builtin = locals
_magic_number = 6
_modified_time = 1392805205.675713
_template_filename=u'templates/webapps/galaxy/galaxy.masthead.mako'
_template_uri=u'/webapps/galaxy/galaxy.masthead.mako'
_template_cache=cache.Cache(__name__, _modified_time)
_source_encoding='ascii'
_exports = ['load', 'get_user_json']


def render_body(context,**pageargs):
    context.caller_stack._push_frame()
    try:
        __M_locals = __M_dict_builtin(pageargs=pageargs)
        __M_writer = context.writer()
        # SOURCE LINE 28
        __M_writer(u'\n\n')
        # SOURCE LINE 118
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_load(context,active_view=None):
    context.caller_stack._push_frame()
    try:
        h = context.get('h', UNDEFINED)
        app = context.get('app', UNDEFINED)
        trans = context.get('trans', UNDEFINED)
        bool = context.get('bool', UNDEFINED)
        def get_user_json():
            return render_get_user_json(context)
        __M_writer = context.writer()
        # SOURCE LINE 31
        __M_writer(u'\n    ')
        # SOURCE LINE 32

        ## get configuration
        masthead_config = {
            ## inject configuration
            'brand'                     : app.config.get("brand", ""),
            'nginx_upload_path'         : app.config.get("nginx_upload_path", h.url_for(controller='api', action='tools')),
            'use_remote_user'           : app.config.use_remote_user,
            'remote_user_logout_href'   : app.config.remote_user_logout_href,
            'enable_cloud_launch'       : app.config.get_bool('enable_cloud_launch', False),
            'lims_doc_url'              : app.config.get("lims_doc_url", "http://main.g2.bx.psu.edu/u/rkchak/p/sts"),
            'biostar_url'               : app.config.biostar_url,
            'biostar_url_redirect'      : h.url_for(controller='biostar', action='biostar_redirect', biostar_action='show/tag/galaxy'),
            'support_url'               : app.config.get("support_url", "http://wiki.galaxyproject.org/Support"),
            'search_url'                : app.config.get("search_url", "http://galaxyproject.org/search/usegalaxy/"),
            'mailing_lists'             : app.config.get("mailing_lists", "http://wiki.galaxyproject.org/MailingLists"),
            'screencasts_url'           : app.config.get("screencasts_url", "http://vimeo.com/galaxyproject"),
            'wiki_url'                  : app.config.get("wiki_url", "http://galaxyproject.org/"),
            'citation_url'              : app.config.get("citation_url", "http://wiki.galaxyproject.org/CitingGalaxy"),
            'terms_url'                 : app.config.get("terms_url", ""),
            'allow_user_creation'       : app.config.allow_user_creation,
            'logo_url'                  : h.url_for(app.config.get( 'logo_url', '/')),
            'is_admin_user'             : trans.user and app.config.is_admin_user(trans.user),
            'active_view'               : active_view,
            'ftp_upload_dir'            : app.config.get("ftp_upload_dir",  None),
            'ftp_upload_site'           : app.config.get("ftp_upload_site",  None),
        
            ## user details
            'user'          : {
                'requests'  : bool(trans.user and (trans.user.requests or trans.app.security_agent.get_accessible_request_types(trans, trans.user))),
                'email'     : trans.user.email if (trans.user) else "",
                'valid'     : bool(trans.user != None),
                'json'      : get_user_json()
            }
        }
            
        
        # SOURCE LINE 66
        __M_writer(u'\n\n    ')
        # SOURCE LINE 68
        __M_writer(unicode(h.templates( "helpers-common-templates" )))
        __M_writer(u'\n    ')
        # SOURCE LINE 69
        __M_writer(unicode(h.js( "mvc/base-mvc", "utils/localization", "mvc/user/user-model", "mvc/user/user-quotameter" )))
        __M_writer(u'\n\n')
        # SOURCE LINE 72
        __M_writer(u'    <script type="text/javascript">\n        if( !window.Galaxy ){\n            window.Galaxy = {};\n        }\n')
        # SOURCE LINE 77
        __M_writer(u'        Galaxy.currUser = new User(')
        __M_writer(unicode( h.to_json_string( get_user_json(), indent=2 ) ))
        __M_writer(u');\n\n')
        # SOURCE LINE 80
        __M_writer(u'        if (window != window.top)\n            $(\'<link href="\' + galaxy_config.root + \'static/style/galaxy.frame.masthead.css" rel="stylesheet">\').appendTo(\'head\');\n\n')
        # SOURCE LINE 84
        __M_writer(u"        $(function() {\n            require(['galaxy.masthead', 'galaxy.menu', 'galaxy.modal', 'galaxy.frame', 'mvc/upload/upload-view'],\n            function(mod_masthead, mod_menu, mod_modal, mod_frame, GalaxyUpload)\n            {\n")
        # SOURCE LINE 89
        __M_writer(u'                if (Galaxy.masthead)\n                    return;\n\n')
        # SOURCE LINE 93
        __M_writer(u'                var masthead_config = ')
        __M_writer(unicode( h.to_json_string( masthead_config ) ))
        __M_writer(u';\n\n')
        # SOURCE LINE 96
        __M_writer(u'                Galaxy.masthead = new mod_masthead.GalaxyMasthead(masthead_config);\n                Galaxy.modal = new mod_modal.GalaxyModal();\n                Galaxy.frame = new mod_frame.GalaxyFrame();\n\n')
        # SOURCE LINE 101
        __M_writer(u'                Galaxy.menu = new mod_menu.GalaxyMenu({\n                    masthead: Galaxy.masthead,\n                    config: masthead_config\n                });\n                \n')
        # SOURCE LINE 107
        __M_writer(u'                Galaxy.upload = new GalaxyUpload(masthead_config);\n\n')
        # SOURCE LINE 111
        __M_writer(u"                Galaxy.quotaMeter = new UserQuotaMeter({\n                    model   : Galaxy.currUser,\n                    el      : $(Galaxy.masthead.el).find('.quota-meter-container')\n                }).render();\n            });\n        });\n    </script>\n")
        return ''
    finally:
        context.caller_stack._pop_frame()


def render_get_user_json(context):
    context.caller_stack._push_frame()
    try:
        AssertionError = context.get('AssertionError', UNDEFINED)
        int = context.get('int', UNDEFINED)
        trans = context.get('trans', UNDEFINED)
        float = context.get('float', UNDEFINED)
        util = context.get('util', UNDEFINED)
        __M_writer = context.writer()
        # SOURCE LINE 2
        __M_writer(u'\n')
        # SOURCE LINE 3

        """Bootstrapping user API JSON"""
        #TODO: move into common location (poss. BaseController)
        if trans.user:
            user_dict = trans.user.to_dict( view='element', value_mapper={ 'id': trans.security.encode_id,
                                                                                 'total_disk_usage': float } )
            user_dict[ 'quota_percent' ] = trans.app.quota_agent.get_percent( trans=trans )
            users_api_controller = trans.webapp.api_controllers[ 'users' ]
            user_dict[ 'tags_used' ] = users_api_controller.get_user_tags_used( trans, user=trans.user )
        else:
            usage = 0
            percent = None
            try:
                usage = trans.app.quota_agent.get_usage( trans, history=trans.history )
                percent = trans.app.quota_agent.get_percent( trans=trans, usage=usage )
            except AssertionError, assertion:
                # no history for quota_agent.get_usage assertion
                pass
            user_dict = {
                'total_disk_usage'      : int( usage ),
                'nice_total_disk_usage' : util.nice_size( usage ),
                'quota_percent'         : percent
            }
        return user_dict
        
        
        # SOURCE LINE 27
        __M_writer(u'\n')
        return ''
    finally:
        context.caller_stack._pop_frame()


