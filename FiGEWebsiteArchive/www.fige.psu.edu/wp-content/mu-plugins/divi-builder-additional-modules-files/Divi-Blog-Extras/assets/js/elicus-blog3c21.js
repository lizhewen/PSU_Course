"use strict";
jQuery(document).ready(function($){
    
    $('body').on('click', '.el-load-more', function(){
        
        var elem    = $(this);
        var blog    = elem.parents('.el-dbe-blog-extra');
        var load    = elem.attr('data-load');
        var total   = elem.attr('data-total');
        var color   = elem.css('color');
        var params  = [];
        
        elem.remove();
        
        blog.find('.ajax-pagination').append('<span class="el-loading" style="color: '+color+';">Loading</span>');
        
        blog.next('.el-blog-params').find('input').each(function(){
            $.each(this.attributes, function() {
                if( $(this).val() !== 'hidden' ){
                    params.push($(this).val());
                }
            });
        });
           
        $.ajax({
			type: "POST",
			url: ajax_object.ajaxurl,
			data: {
				action: 'el_load_posts',
				security: ajax_object.ajax_nonce,
				page: load,
				total_pages: total,
				parameters: params,
			},
			success: function (response) {
			    setTimeout(function(){
			        blog.find('.ajax-pagination').remove();
    				blog.append(response);
    				setTimeout(function(){
    				    blog.find('.et_pb_post_extra').each(function(){
    				        $(this).fadeTo('1000','1');
    				    });
    				},200);
    				setTimeout(function(){
    				    blog.find('.ajax-pagination').find('.el-load-more').fadeIn('800');
    				    blog.find('.ajax-pagination').find('.el-show-less').fadeIn('800');
    				},500);
			    },1500);
			},
			error: function () {
			  alert('Oops!! Something went wrong!! Try later!');
			}
		});
		
    });
    
    $('body').on('click', '.el-show-less', function(){
        
        var elem                = $(this);
        var blog                = elem.parents('.el-dbe-blog-extra');
        var num                 = parseInt(elem.attr('data-num'));
        var total               = parseInt(elem.attr('data-total'));
        var load_more_text      = elem.attr('data-load-more-text');
        var color               = elem.css('color');
        
        elem.remove();
        
        blog.find('.ajax-pagination').append('<span class="el-loading" style="color: '+color+';">Loading</span>');
        
        setTimeout(function(){
            setTimeout(function(){ 
                blog.find('.el-loading').remove();
                blog.find('.et_pb_post_extra:not(:lt('+(num)+'))').fadeOut('1000');
                blog.find('.ajax-pagination').append('<a class="et_pb_button el-button el-load-more" data-load="1" data-total="'+total+'"><span class="btn-label">'+load_more_text+'</span></a>');
            },200);
            setTimeout(function(){
                blog.find('.et_pb_post_extra:not(:lt('+(num)+'))').remove();
            },1000);
        },1500);
        
    });
    
});