function [img] = recoverImg(gausLstLyr,lapPyra)
    n = length(lapPyra);
    result = cell(n+1);
    result{n+1} = gausLstLyr;
    for i = n:-1:1
        % across diff size of layers
        if rem(i,3) == 0
            upsamplexy = upsample(result{i+1});
            result{i} = double(lapPyra{i}) + double(upsamplexy);
        else
            result{i} = double(lapPyra{i}) + double(result{i+1});
        end
    end
    img = uint8(result{1});
end